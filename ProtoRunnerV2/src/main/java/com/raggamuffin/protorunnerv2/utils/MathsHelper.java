package com.raggamuffin.protorunnerv2.utils;

import java.util.Random;

public class MathsHelper 
{
	public final static double PI_OVER_2 = Math.PI / 2;
	
	private static final Random Rand = new Random();
	
	// Linear Interpolation.
	public static double Lerp(double Amount, double Min, double Max)
	{
		return (Min * (1.0f - Amount) + (Max * Amount));
	}
	
	// Normalise a value in range 0 - 1.
	public static double Normalise(double Amount, double Min, double Max)
	{
		return Clamp((Amount - Min) / (Max - Min),0.0, 1.0);
	}
	
	// Normalise a value in range -1 - 1.
	public static double SignedNormalise(double Amount, double Min, double Max)
	{
		return Clamp((Normalise(Amount, Min, Max) - 0.5) * 2, -1.0, 1.0);
	}

	// Clamp a value between two values.
	public static double Clamp(double Amount, double Min, double Max)
	{
		if(Amount > Max)
			return Max;
		
		if(Amount < Min)
			return Min;
			
		return Amount;
	}

    public static int ClampInt(int amount, int min, int max)
    {
        if(amount > max)
            return max;

        if(amount < min)
            return min;

        return amount;
    }
	
	// Returns a random double within in range Min - Max.
	public static double RandomDouble(double Min, double Max)
	{
		return (Rand.nextDouble() * (Max - Min)) + Min;
	}
	
	// Returns a random int within the range Min -Max.
	public static int RandomInt(int Min, int Max)
	{
		return Rand.nextInt((Max - Min)) + Min;
	}
	
	public static boolean RandomBoolean()
	{
		return Rand.nextBoolean();
	}

	// Convert Degrees into radians.
	public static double DegToRad(double Degrees)
	{
		return Degrees * (Math.PI / 180);
	}
	
	// Convert Radians into Degrees.
	public static double RadToDeg(double Radians)
	{
		return Radians * (180 / Math.PI);
	}
	
	public static double FastInverseSqrt(float x)
	{
		return (double)Float.intBitsToFloat(0x5f3759d5 - (Float.floatToIntBits(x) >> 1));
	}
}