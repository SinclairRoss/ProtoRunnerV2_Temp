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
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Weapon_PulseLaser;
import com.raggamuffin.protorunnerv2.weapons.Weapon_PulseLaserWingman;

public class Vehicle_Wingman extends Vehicle
{
	private AIController m_AIController;
	private VehicleManager m_VehicleManager;

    private Subscriber m_PlayerSpawnedSubscriber;
	
	public Vehicle_Wingman(GameLogic game, Vector3 position)
	{
		super(game, ModelType.Runner, position, 1.5, 1, VehicleClass.StandardVehicle, true, PublishedTopics.WingmanDestroyed, AffiliationKey.BlueTeam);
		
		m_VehicleManager = game.GetVehicleManager();

        SetColour(Colours.RunnerBlue);

        m_Engine = new Engine_Standard(this, game);
		m_Engine.SetMaxTurnRate(2.0);
		m_Engine.SetMaxEngineOutput(70);
        m_Engine.SetAfterBurnerOutput(90);
		
		SelectWeapon(new Weapon_PulseLaserWingman(this, game));

		NavigationalBehaviourInfo navInfo = new NavigationalBehaviourInfo(0.4, 1.0, 0.7, 0.6);
        m_AIController = new AIController(this, m_VehicleManager, game.GetBulletManager(), navInfo, AIBehaviours.FollowTheLeader, FireControlBehaviour.Standard, TargetingBehaviour.Standard);
		m_AIController.SetLeader(m_VehicleManager.GetPlayer());

        m_PlayerSpawnedSubscriber = new PlayerSpawnedSubscriber();
		game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, m_PlayerSpawnedSubscriber);
	}

	@Override 
	public void Update(double deltaTime)
	{
		m_AIController.Update(deltaTime);
		
		super.Update(deltaTime);
	}

    @Override
    public void CleanUp()
    {
		super.CleanUp();
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.PlayerSpawned, m_PlayerSpawnedSubscriber);
		m_AIController.CleanUp();
    }

    private class PlayerSpawnedSubscriber extends Subscriber
	{
		@Override
		public void Update(Object args)
		{
			m_AIController.SetLeader(m_VehicleManager.GetPlayer());
		}	
	}
}