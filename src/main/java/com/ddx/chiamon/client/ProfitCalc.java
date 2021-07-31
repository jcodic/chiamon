package com.ddx.chiamon.client;

import com.ddx.chiamon.utils.Str;
import com.ddx.chiamon.utils.Utils;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author ddx
 */
public class ProfitCalc {

    private static void printInfo() {
        
        System.out.println("Usage: profit_calc <total_disk_space_tb> <your_disk_space_tb> <coin_price_usd> [rub_per_usd]");
    }

    public static void main(String[] args) {

        try {

            if (args != null && args.length < 3) {
                
                printInfo();
                return;
            }
  
            for (int i = 0; i < args.length ; i++)
                args[i] = args[i].replaceAll("_", "");
            
            DecimalFormat df = (DecimalFormat)NumberFormat.getInstance(Locale.US);
            DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            df.setDecimalFormatSymbols(symbols);
            df.setMaximumFractionDigits(20);
            
            double totalX = Double.parseDouble(args[0]);
            double yourX = Double.parseDouble(args[1]);
            double coinPrice = Double.parseDouble(args[2]);
            boolean useRuble = args.length == 4;
            double rubUsd = useRuble?Double.parseDouble(args[3]):0.0d;
            
            double oneXFoundsDaily = 4608.0d / totalX;
            double yourXFoundsDaily = yourX * oneXFoundsDaily;
            double yourXFoundsPerMonth = yourXFoundsDaily * 30.0d;
            double yourXFoundsPerYear = yourXFoundsDaily * 365.0d;
            double daysToFindABlock = 1.0d / yourXFoundsDaily;
            double yourXshare = yourX * 100.0d / totalX;
            
            double oneXProfitDaily = oneXFoundsDaily * 2.0d * coinPrice;
            double oneXProfitPerMonth = oneXProfitDaily * 30.0d;
            double oneXProfitPerYear = oneXProfitDaily * 365.0d;

            double yourProfitDaily = yourXFoundsDaily * 2.0d * coinPrice;
            double yourProfitPerMonth = yourProfitDaily * 30.0d;
            double yourProfitPerYear = yourProfitDaily * 365.0d;

            double totalNetPayoutDaily = 4608.0d * 2.0d * coinPrice; 
            double totalNetPayoutPerMonth = totalNetPayoutDaily * 30.0d; 
            double totalNetPayoutPerYear = totalNetPayoutDaily * 365.0d; 

            double yourChanceToFindBlockDaily = yourXFoundsDaily > 1.0d ? 1.0d : yourXFoundsDaily;
            double yourChanceToFindBlockPerMonth = 1.0d - Math.pow(1.0d - yourChanceToFindBlockDaily, 30.0d);
            double yourChanceToFindBlockPerYear = 1.0d - Math.pow(1.0d - yourChanceToFindBlockDaily, 365.0d);
            
            String curr = "usd";
                    
            if (useRuble) {
                
                curr = "rub";
                oneXProfitDaily *= rubUsd;
                oneXProfitPerMonth *= rubUsd;
                oneXProfitPerYear *= rubUsd;
                yourProfitDaily *= rubUsd;
                yourProfitPerMonth *= rubUsd;
                yourProfitPerYear *= rubUsd;
            }
            
            
            
            int sp = 40;
            System.out.println(Str.getStringWPrefix("Total disk space, Tb", sp, " ", false)+": "+df.format(totalX));
            System.out.println(Str.getStringWPrefix("Your disk space, Tb", sp, " ", false)+": "+df.format(yourX));
            System.out.println(Str.getStringWPrefix("Your share of total, %", sp, " ", false)+": "+df.format(yourXshare));
            System.out.println(Str.getStringWPrefix("Coin price, usd", sp, " ", false)+": "+coinPrice);
            System.out.println(Str.getStringWPrefix("", sp, "-"));
            System.out.println(Str.getStringWPrefix("Total net payout per day, usd", sp, " ", false)+": "+df.format(totalNetPayoutDaily));
            System.out.println(Str.getStringWPrefix("Total net payout per month, usd", sp, " ", false)+": "+df.format(totalNetPayoutPerMonth));
            System.out.println(Str.getStringWPrefix("Total net payout per year, usd", sp, " ", false)+": "+df.format(totalNetPayoutPerYear));
            System.out.println(Str.getStringWPrefix("", sp, "-"));
            System.out.println(Str.getStringWPrefix("Your disk space find ->", sp, " ", false));
            System.out.println(Str.getStringWPrefix("Blocks per day", sp, " ", false)+": "+Utils.RoundResult(yourXFoundsDaily, 3));
            System.out.println(Str.getStringWPrefix("Blocks per month", sp, " ", false)+": "+Utils.RoundResult(yourXFoundsPerMonth, 3));
            System.out.println(Str.getStringWPrefix("Blocks per year", sp, " ", false)+": "+Utils.RoundResult(yourXFoundsPerYear, 3));
            System.out.println(Str.getStringWPrefix("Days to find a block", sp, " ", false)+": "+Utils.RoundResult(daysToFindABlock, 1));
            System.out.println(Str.getStringWPrefix("Profit per day, "+curr, sp, " ", false)+": "+df.format(Utils.RoundResult(yourProfitDaily, 2)));
            System.out.println(Str.getStringWPrefix("Profit per month, "+curr, sp, " ", false)+": "+df.format(Utils.RoundResult(yourProfitPerMonth, 1)));
            System.out.println(Str.getStringWPrefix("Profit per year, "+curr, sp, " ", false)+": "+df.format(Utils.RoundResult(yourProfitPerYear, 1)));
            System.out.println(Str.getStringWPrefix("", sp, "-"));
            System.out.println(Str.getStringWPrefix("Your chance to find block ->", sp, " ", false));
            System.out.println(Str.getStringWPrefix("per day, %", sp, " ", false)+": "+df.format(yourChanceToFindBlockDaily * 100.0d));
            System.out.println(Str.getStringWPrefix("per month, %", sp, " ", false)+": "+df.format(yourChanceToFindBlockPerMonth * 100.0d));
            System.out.println(Str.getStringWPrefix("per year, %", sp, " ", false)+": "+df.format(yourChanceToFindBlockPerYear * 100.0d));
            
            System.out.println(Str.getStringWPrefix("", sp, "-"));
            System.out.println(Str.getStringWPrefix("1 Tb profit per day, "+curr, sp, " ", false)+": "+df.format(Utils.RoundResult(oneXProfitDaily, 2)));
            System.out.println(Str.getStringWPrefix("1 Tb profit per month, "+curr, sp, " ", false)+": "+df.format(Utils.RoundResult(oneXProfitPerMonth, 1)));
            System.out.println(Str.getStringWPrefix("1 Tb profit per year, "+curr, sp, " ", false)+": "+df.format(Utils.RoundResult(oneXProfitPerYear, 1)));
            
        } catch (Exception ex) { ex.printStackTrace(); }
    
    }
    
}
