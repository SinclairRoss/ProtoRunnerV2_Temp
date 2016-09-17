package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.ai.NavigationalBehaviourInfo;
import com.raggamuffin.protorunnerv2.ai.TargetingBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

import java.util.ArrayList;

public class Vehicle_Carrier extends Vehicle
{
	private AIController m_AIController;
	private VehicleManager m_VehicleManager;

    private final int DRONE_CAPACITY = 3;

	public Vehicle_Carrier(GameLogic game)
	{
		super(game, ModelType.Carrier);
		
		m_VehicleManager = game.GetVehicleManager();

        SetColourScheme(Colours.EmeraldGreen, Colours.BlockPurple);

        m_MaxHullPoints = 300;
        m_HullPoints = m_MaxHullPoints;

		m_Position.SetVector(10, 0, 10);

        m_Mass = 100;
        m_Engine = new Engine_Cycling(this, game);
		m_Engine.SetMaxTurnRate(1.0);
		m_Engine.SetMaxEngineOutput(10000); // 10000
        m_Engine.SetDodgeOutput(0);
        m_BoundingRadius = 5;

		SetAffiliation(AffiliationKey.RedTeam);

        SelectWeapon(new Weapon_None(this, game));

        NavigationalBehaviourInfo navInfo = new NavigationalBehaviourInfo(0.4, 1.0, 0.7, 0.6);
		m_AIController = new AIController(this, m_VehicleManager, game.GetBulletManager(), navInfo, AIBehaviours.Encircle, FireControlBehaviour.None, TargetingBehaviour.Standard);

		m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyDestroyed);

        CreateDrones(DRONE_CAPACITY);
    }
	
	@Override 
	public void Update(double deltaTime)
	{
		m_AIController.Update(deltaTime);
		super.Update(deltaTime);
	}

    private void CreateDrones(int count)
    {
        for(int i = 0; i < count; i++)
        {
            m_VehicleManager.SpawnDrone(this);
        }
    }

    @Override
    public void CleanUp()
    {
        super.CleanUp();
        m_PrimaryWeapon.CleanUp();
    }
} 