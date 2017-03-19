package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_Runner;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;

public class TutorialEvent_Immortality extends TutorialEvent
{
    private VehicleManager m_VehicleManager;
    private boolean m_On;

    public TutorialEvent_Immortality(GameLogic game)
    {
        super(game);

        m_VehicleManager = game.GetVehicleManager();
        m_On = false;
    }

    @Override
    public void On()
    {
        m_On = true;
    }

    @Override
    public void Off()
    {
        m_On = false;
    }

    @Override
    public void Update()
    {
        Vehicle_Runner player = m_VehicleManager.GetPlayer();

        if (player == null)
            return;

        if(m_On)
        {
            player.SetHullPoints(player.GetMaxHullPoints());
        }
        else
        {
            double hullPointMin = player.GetHullPoints() / 4;

            if(player.GetHullPoints() < hullPointMin)
            {
                player.SetHullPoints(hullPointMin);
            }
        }
    }
}
