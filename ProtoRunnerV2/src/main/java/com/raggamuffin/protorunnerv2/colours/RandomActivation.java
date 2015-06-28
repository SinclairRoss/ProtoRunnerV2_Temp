// Author:	Sinclair Ross.
// Date:	24/10/2014.
// Notes:	Triggers the colour at randomly set intervals.

package com.raggamuffin.protorunnerv2.colours;

import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class RandomActivation extends ActivationBehaviour
{

	private double m_ActivationTimer;
	private double m_MinActivationTime;
	private double m_MaxActivationTime;
	
	public RandomActivation(ColourBehaviour Anchor) 
	{
		super(Anchor);

		m_MinActivationTime = 5.0;
		m_MaxActivationTime = 60.0;
			
		m_ActivationTimer = MathsHelper.RandomDouble(m_MinActivationTime,m_MaxActivationTime);
	}

	@Override
	protected void UpdateTrigger(double DeltaTime) 
	{
		m_ActivationTimer -= DeltaTime;
		
		if(m_ActivationTimer <= 0.0)
		{
			m_Anchor.TriggerBehaviour();

			m_ActivationTimer =  MathsHelper.RandomDouble(m_MinActivationTime,m_MaxActivationTime);
		}
	}
}