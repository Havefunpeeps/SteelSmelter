import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;


public class Wait extends Task<ClientContext>{

	private final int IRON_ORE_ID = 440;
	private final int COAL_ID = 453;
	private final int STEEL_BAR_ID = 2353;
	private final int SMELTER_ID = 76293;
	
	private static int numSteel;
	
	public Wait(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return isSmelting();
				
	}

	@Override
	public void execute() {
		SteelSmelter.status = "Wait";
		SteelSmelter.numSmeltedThisTrip = ctx.backpack.select().id(STEEL_BAR_ID).count();
		
		performAntiBan();
	}
	
	public boolean isSmelting() {
		Component smeltingWidget = ctx.widgets.component(1251, 0);
		return smeltingWidget.visible();
	}

	@SuppressWarnings("deprecation")
	public void performAntiBan() {
		double rand = Math.random();
		if (rand > .9) { //Perform mouse antiban
			double currentMouseX = ctx.mouse.getLocation().getX();
			double currentMouseY = ctx.mouse.getLocation().getY();
			ctx.mouse.move((int)currentMouseX + Random.nextInt(-3000, 3000), (int)currentMouseY + Random.nextInt(-3000, 3000));
			sleep(Random.nextInt(4000, 10000));		
		} else { //Perform camera antiban
			Tile myTile = ctx.players.local().tile();
			int tileX = myTile.x() + Random.nextInt(-10, 10);
			int tileY = myTile.y() + Random.nextInt(-10, 10);
			ctx.camera.turnTo(new Tile(tileX, tileY));
			sleep(Random.nextInt(4000, 10000));
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
}
