import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.powerbot.script.Filter;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GeItem;
import org.powerbot.script.rt6.Item;

@Script.Manifest(name = "Steel Smelter", description = "Smelts steel at Al-Kharid")

public class SteelSmelter extends PollingScript<ClientContext> implements PaintListener {

	static String status = "";
	static long startTime = 0;
    
	long millis = 0;
	long hours = 0;
	long minutes = 0;
	long seconds = 0;
	
	static int totalNumSmelted = 0;
	static int numSmeltedThisTrip = 0;
	
	static int IRON_ORE_PRICE = 0;
	static int COAL_PRICE = 0;
	static int STEEL_BAR_PRICE = 0;
	
	private final int IRON_ORE_ID = 440;
	private final int COAL_ID = 453;
	private final int STEEL_BAR_ID = 2353;
	private final int SMELTER_ID = 76293;
	private final int BANK_ID = 76274;
	private final int COAL_BAG_ID = 18339;
	
	static boolean usingCoalBag;
	
	
	private List<Task> taskList = new ArrayList<Task>();
	
	@Override
	public void start() {
		startTime = System.currentTimeMillis();
		IRON_ORE_PRICE = GeItem.price(IRON_ORE_ID);
		COAL_PRICE = GeItem.price(COAL_ID);
		STEEL_BAR_PRICE = GeItem.price(STEEL_BAR_ID);
		usingCoalBag = ctx.backpack.select().id(COAL_BAG_ID).count() > 0;
		
		taskList.addAll(Arrays.asList(new Smelt(ctx), new Wait(ctx), new WalkToBank(ctx), new WalkToSmelter(ctx), new Bank(ctx)));
	}
	
	@Override
	public void poll() {
        for (Task task : taskList) {
            if (task.activate()) {
                task.execute();
            }
        }
	}
	

	public void repaint(Graphics g) {
	    final Color color1 = new Color(91, 160, 206, 200); //123 was too light, lower # == more opaque
	    final Color color2 = new Color(0, 0, 0);

	    final Font font1 = new Font("Arial", 0, 12);
	    
	    int smeltedHour = (int) ((totalNumSmelted + numSmeltedThisTrip) * 3600000D / (System.currentTimeMillis() - startTime));
	    int profitPerBar = (STEEL_BAR_PRICE - (IRON_ORE_PRICE + (2 * COAL_PRICE)));
	    int totalProfit = profitPerBar * (totalNumSmelted + numSmeltedThisTrip);
	    int profitHour = (int) (profitPerBar * smeltedHour);
	    int expGained = (int) (17.5 * (totalNumSmelted + numSmeltedThisTrip));
	    int expHour = (int) (17.5 * smeltedHour);

	    millis = System.currentTimeMillis() - startTime;
		millis -= hours * (1000 * 60 * 60);
		minutes = millis / (1000 * 60);
		millis -= minutes * (1000 * 60);
		seconds = millis / 1000;
		
        g.setColor(color1);
        g.fillRoundRect(7, 243, 146, 119, 16, 16);
        g.setColor(color2);
        g.drawRoundRect(7, 243, 146, 119, 16, 16);
        g.setFont(font1);
        g.drawString("Status: " + status, 11, 258);
        g.drawString("Num Smelted: " + (totalNumSmelted + numSmeltedThisTrip), 11, 271);
        g.drawString("Smelted p/h: " + smeltedHour, 11, 284);
        g.drawString("Profit: " + totalProfit, 11, 297);
        g.drawString("Profit p/h: " + profitHour, 11, 309);
        g.drawString("Exp Gained: " + expGained, 11, 322);
        g.drawString("Exp p/h: " + expHour, 11, 335);
        g.drawString("Time Ran: "+ hours +":"+ minutes + ":" + seconds, 11, 348);
        g.drawString("Using Coal Bag: " + usingCoalBag, 11, 361);
	}

}


