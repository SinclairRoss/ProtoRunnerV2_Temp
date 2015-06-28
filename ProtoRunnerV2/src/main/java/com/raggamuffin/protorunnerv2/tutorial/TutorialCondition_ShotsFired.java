package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Runner;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class TutorialCondition_ShotsFired extends TutorialCondition
{
    private int m_MaxAmount;
    private int m_Amount;

    public TutorialCondition_ShotsFired(GameLogic game, String message, int amount)
    {
        super(game, message);

        m_MaxAmount = amount;
        m_Amount = 0;
    }

    @Override
    public void Update(double deltaTime)
    {
        Runner player = m_Game.GetVehicleManager().GetPlayer();

        if(player != null)
        {
            if(player.GetPrimaryWeapon().IsFiring())
            {
                m_Amount ++;
            }
        }
    }

    @Override
    public boolean ConditionComplete()
    {
        return m_Amount >= m_MaxAmount;
    }

    @Override
    public double GetProgress()
    {
        return MathsHelper.Normalise(m_Amount, 0, m_MaxAmount);
    }
}