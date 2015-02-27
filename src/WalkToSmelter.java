import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;


public class WalkToSmelter extends Task<ClientContext>{

	private final int IRON_ORE_ID = 440;
	private final int COAL_ID = 453;
	private final int STEEL_BAR_ID = 2353;
	private final int SMELTER_ID = 76293;
	
	public WalkToSmelter(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().id(IRON_ORE_ID).count() > 0 &&
				ctx.backpack.select().id(COAL_ID).count() > 0 &&
				ctx.backpack.select().id(STEEL_BAR_ID).count() == 0 &&
				ctx.players.local().tile().distanceTo(ctx.objects.select().id(SMELTER_ID).nearest().poll()) > 5;
				
	}

	@Override
	public void execute() {
		SteelSmelter.status = "Walking To Smelter";
		
		GameObject smelter = ctx.objects.select().id(SMELTER_ID).poll();
		
		if (!ctx.players.local().inMotion()) {
			ctx.movement.step(smelter.tile().derive(1, 1));
		}
		
		Condition.sleep(Random.nextInt(300, 700));
	}

}
