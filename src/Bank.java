import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;


public class Bank extends Task<ClientContext>{

	private final int IRON_ORE_ID = 440;
	private final int COAL_ID = 453;
	private final int STEEL_BAR_ID = 2353;
	private final int SMELTER_ID = 76293;
	private final int BANK_ID = 76274;
	private final int COAL_BAG_ID = 18339;
	
	public Bank(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return (ctx.backpack.select().id(STEEL_BAR_ID).count() > 0 || ctx.backpack.select().id(IRON_ORE_ID).count() == 0 || ctx.backpack.select().id(COAL_ID).count() == 0) &&
				ctx.players.local().tile().distanceTo(ctx.objects.select().id(BANK_ID).nearest().poll()) < 5;
				
	}

	@Override
	public void execute() {
		SteelSmelter.status = "Bank";
		
		SteelSmelter.totalNumSmelted += SteelSmelter.numSmeltedThisTrip;
		SteelSmelter.numSmeltedThisTrip = 0;

		while (!ctx.bank.opened()) {
			ctx.bank.open();
			Condition.sleep(Random.nextInt(100, 300));
		}
		
		withdrawItems(SteelSmelter.usingCoalBag);
		
	}
	
	public void withdrawItems(boolean usingCoalBag) {
		if (ctx.bank.opened()) {
			if (ctx.backpack.select().id(STEEL_BAR_ID).count() > 0) {
				ctx.bank.deposit(STEEL_BAR_ID, 0);
			} 
			
			if (!usingCoalBag) {
				if (getCountInBank(IRON_ORE_ID) < 9 || getCountInBank(COAL_ID) < 18) {
					ctx.controller.stop();
				}
				ctx.bank.withdraw(IRON_ORE_ID, 9);
				ctx.bank.withdraw(COAL_ID, 0);
			} else {
				if (getCountInBank(IRON_ORE_ID) < 18 || getCountInBank(COAL_ID) < 36) {
					ctx.controller.stop();
				}
				final Item coalBag = ctx.backpack.select().id(COAL_BAG_ID).poll();
				coalBag.interact("Fill");
				ctx.bank.withdraw(IRON_ORE_ID, 18);
				ctx.bank.withdraw(COAL_ID, 0);
			}
		}		
	}
	
	public int getCountInBank(int id) {
		int index = ctx.bank.indexOf(id);
		if (index == -1) {
			return 0;
		} else {
			return ctx.bank.itemAt(index).stackSize();
		}
	}
}
