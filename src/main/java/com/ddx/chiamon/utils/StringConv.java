package com.ddx.chiamon.utils;

/**
 *
 * @author ddx
 */
public class StringConv {
    

    public static int[] String2IntArray(String source) {

	String[] inp = String2Array(source);
	
	if (inp == null) return null;

	int[] res = new int[inp.length];
	
	for (int i = 0; i < inp.length; i++) res[i] = Integer.parseInt(inp[i]);
	
	return res;
    }

    public static long[] String2LongArray(String source) {

	String[] inp = String2Array(source);
	
	if (inp == null) return null;

	long[] res = new long[inp.length];
	
	for (int i = 0; i < inp.length; i++) res[i] = Long.parseLong(inp[i]);
	
	return res;
    }

    public static double[] String2DoubleArray(String source) {

	String[] inp = String2Array(source);
	
	if (inp == null) return null;

	double[] res = new double[inp.length];
	
	for (int i = 0; i < inp.length; i++) res[i] = Double.parseDouble(inp[i]);
	
	return res;
    }

    public static String[] String2Array(String source) {

	if (source == null || source.length() == 0) return null;
	
	source = source.replaceAll("\\s+","");
	
	if (source.contains(",")) {
	    
	    return source.split(",");
	} else {
	    
	    return new String[]{source};
	}
    }

    public static String[] String2Range(String source) throws Exception {

	boolean suits = source != null && source.contains("-");
	
	if (!suits) return null;
	
	source = source.replace(" ", "");
        String[] inp = source.split("-");

	if (inp.length != 2) return null;

	return inp;
    }

    public static int[] String2IntRange(String source) throws Exception {
        
        String[] inp = String2Range(source);
        
        if (inp == null) return null;

        return new int[]{Integer.parseInt(inp[0]),Integer.parseInt(inp[1])};
    }
    
}