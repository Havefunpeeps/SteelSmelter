import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;


public class Smelt extends Task<ClientContext>{

	private final int IRON_ORE_ID = 440;
	private final int COAL_ID = 453;
	private final int STEEL_BAR_ID = 2353;
	private final int SMELTER_ID = 76293;
	
	public Smelt(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		final GameObject smelter = ctx.objects.select().id(SMELTER_ID).nearest().poll();
		
		return !isSmelting() &&
				ctx.backpack.select().id(IRON_ORE_ID).count() > 0 &&
				ctx.backpack.select().id(COAL_ID).count() > 0 &&
				ctx.backpack.select().id(STEEL_BAR_ID).count() == 0 &&
				smelter.inViewport();
				
	}

	@Override
	public void execute() {
		SteelSmelter.status = "Smelt";
		// TODO Auto-generated method stub
		final GameObject smelter = ctx.objects.select().id(SMELTER_ID).nearest().poll();
		final Item coal = ctx.backpack.select().id(COAL_ID).poll();
		Component smeltWidget = ctx.widgets.component(1370, 20);

		while (!ctx.backpack.itemSelected() && !smeltWidget.visible()) {
			coal.interact("Use");
		}
		
		if (ctx.backpack.itemSelected()) {
			smelter.interact("Use");
			sleep(1000);
		}
		
		if (smeltWidget.visible()) {
			smeltWidget.click();
			sleep(1000);
		}
		
	}
	
	public void sleep(int ms) {
		try {
			Thread.sleep(Random.nextInt(ms-100, ms+300));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isSmelting() {
		Component smeltingWidget = ctx.widgets.component(1251, 0);
		return smeltingWidget.visible();
	}

}
