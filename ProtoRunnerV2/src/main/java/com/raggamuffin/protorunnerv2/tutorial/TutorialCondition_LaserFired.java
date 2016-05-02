package com.raggamuffin.protorunnerv2.tutorial;

import android.util.Log;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_Runner;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.weapons.WeaponSlot;

public class TutorialCondition_LaserFired extends TutorialCondition
{
    private Timer m_FireDuration;
    private WeaponSlot m_WeaponSlot;

    public TutorialCondition_LaserFired(GameLogic game, String message, double duration, WeaponSlot slot, TutorialEffect... effects)
    {
        super(game, message, OptionalElement.ProgressBar, effects);

        m_FireDuration = new Timer(duration);
        m_WeaponSlot = slot;
    }

    @Override
    public void Update(double deltaTime)
    {
        Vehicle_Runner player = m_Game.GetVehicleManager().GetPlayer();

        if(player != null)
        {
            if (player.GetWeaponSlot() == m_WeaponSlot)
            {
                if (player.GetPrimaryWeapon().IsTriggerPulled())
                {
                    m_FireDuration.Update(deltaTime);
                }
            }
        }
    }

    @Override
    public boolean ConditionComplete()
    {
        return m_FireDuration.TimedOut();
    }

    @Override
    public double GetProgress()
    {
        return m_FireDuration.GetProgress();
    }

    @Override
    public void Reset()
    {
        super.Reset();
        m_FireDuration.ResetTimer();
    }
}