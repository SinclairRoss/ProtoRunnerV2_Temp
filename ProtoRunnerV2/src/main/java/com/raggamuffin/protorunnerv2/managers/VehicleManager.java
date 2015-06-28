package com.raggamuffin.protorunnerv2.managers;

import java.util.Iterator;
import java.util.Vector;

import android.util.Log;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gamelogic.GameSettings;
import com.raggamuffin.protorunnerv2.gamelogic.GameStats;
import com.raggamuffin.protorunnerv2.gameobjects.Bit;
import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.gameobjects.Runner;
import com.raggamuffin.protorunnerv2.gameobjects.Squaker;
import com.raggamuffin.protorunnerv2.gameobjects.Tank;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.gameobjects.Wingman;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Orbiter;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class VehicleManager
{	
	private final double ExhibitionSpawnDistance = 40.0;
	private final double PlayingSpawnDistance = 100.0;

	private double m_MaxSpawnDistance;
	
	private ControlScheme m_ControlScheme;
	
	private Runner m_Player;
	private Vector<Vehicle> m_Vehicles;
	private Vector<Vehicle> m_BlueTeam;
	private Vector<Vehicle> m_RedTeam;
	
	private GameLogic m_Game;
	
	private Publisher m_PlayerSpawnedPublisher;
	
	private Squaker m_Squaker;
	private Orbiter m_Orbiter;
	
	public VehicleManager(GameLogic Game)
	{
		m_Game = Game;

		m_ControlScheme 	= m_Game.GetControlScheme();
		
		m_Vehicles = new Vector<Vehicle>();
		m_BlueTeam = new Vector<Vehicle>();
		m_RedTeam  = new Vector<Vehicle>();

		m_MaxSpawnDistance = ExhibitionSpawnDistance;

		PubSubHub PubSub = m_Game.GetPubSubHub();
		
		m_PlayerSpawnedPublisher = PubSub.CreatePublisher(PublishedTopics.PlayerSpawned);
		PubSub.SubscribeToTopic(PublishedTopics.StartGame, new StartGameSubscriber());
		PubSub.SubscribeToTopic(PublishedTopics.EndGame, new EndGameSubscriber());
	}
	
	public void Update(double deltaTime)
	{
		// Update Vehicles.
		for(Iterator<Vehicle> Iter = m_Vehicles.iterator(); Iter.hasNext();)
		{
			Vehicle Object = Iter.next();
			
			if(Object.IsValid())
			{
				Object.Update(deltaTime);
			}
			else	
			{
				RemoveVehicle(Object);
				Iter.remove();
			} 
		}
	}

	// Removes vehicle from their team and the renderer.
	// DOES NOT remove vehicle from m_Vehicles.
	private void RemoveVehicle(Vehicle Object)
	{		
		m_Game.RemoveObjectFromRenderer(Object);
		GetTeam(Object.GetAffiliation()).remove(Object);
		
		if(Object == m_Player)
			m_Player = null;
	}

	public void SpawnPlayer()
	{
		// Ensures no more than one player is active at any one a time.
		if(m_Player != null)
			return;
		
		Log.e("Player", "Player spawn.");
		
		m_Player = new Runner(m_Game.GetBulletManager(), m_Game.GetGameAudioManager(), m_Game.GetParticleManager(), this, m_ControlScheme, m_Game.GetPubSubHub(), m_Game.GetContext());
		m_Vehicles.add(m_Player);
		m_BlueTeam.add(m_Player);
		m_Game.AddObjectToRenderer(m_Player);

		m_PlayerSpawnedPublisher.Publish();
	}
	
	private void SpawnSquaker()
	{
		m_Squaker = new Squaker(m_Game.GetPubSubHub(), m_Game.GetGameAudioManager());
		m_Game.AddObjectToRenderer(m_Squaker);
	}
	
	private void SpawnOrbiter()
	{
		m_Orbiter = new Orbiter(m_Game.GetBulletManager(), m_Game.GetParticleManager(), m_Game.GetPubSubHub(), m_Game.GetGameAudioManager());
		m_Game.AddObjectToRenderer(m_Orbiter);
		m_Vehicles.add(m_Orbiter);
		m_RedTeam.add(m_Orbiter);
	}
	
	public void SpawnWingmen()
	{
		Wingman Buddy = new Wingman(m_Game.GetBulletManager(), m_Game.GetGameAudioManager(), m_Game.GetParticleManager(), this, m_Game.GetPubSubHub(), m_Game.GetContext());
		m_Vehicles.add(Buddy);
		m_BlueTeam.add(Buddy);
		m_Game.AddObjectToRenderer(Buddy);		
	}
	
	public void SpawnSquad()
	{
		double I = MathsHelper.RandomDouble(-1.0, 1.0);
		double J = 0;
		double K = MathsHelper.RandomDouble(-1.0, 1.0);
		
		Vector3 m_Spawn = new Vector3(I,J,K);
		m_Spawn.Normalise();
		m_Spawn.Scale(m_MaxSpawnDistance);
		
		if(m_Player != null)
			m_Spawn.Add(m_Player.GetPosition());
		
		for(int b = 0; b < 1; b++)
		{
			Tank bit = new Tank(m_Game.GetBulletManager(), m_Game.GetGameAudioManager(), m_Game.GetParticleManager(), this, m_Game.GetPubSubHub());
			m_Vehicles.add(bit);
			m_RedTeam.add(bit);
			m_Game.AddObjectToRenderer(bit);
			
			bit.SetPosition(m_Spawn.I + b * 2,m_Spawn.J,m_Spawn.K);
		}
		
		for(int b = 0; b < 6; b++)
		{
			Bit bit = new Bit(m_Game.GetBulletManager(), m_Game.GetGameAudioManager(), m_Game.GetParticleManager(), this, m_Game.GetPubSubHub());
			m_Vehicles.add(bit);
			m_RedTeam.add(bit);
			m_Game.AddObjectToRenderer(bit);
			
			bit.SetPosition(m_Spawn.I + b * 2,m_Spawn.J,m_Spawn.K);
		}
	}
	
	public Vector<Vehicle> GetTeam(AffiliationKey faction)
	{
		switch(faction)
		{
			case RedTeam:
				return m_RedTeam;

			case BlueTeam:
				return m_BlueTeam;

			case Neutral:
				return null;

			default:
				return null;
		}
	}
	
	public Vector<Vehicle> GetOpposingTeam(AffiliationKey faction)
	{
		switch(faction)
		{
			case RedTeam:
				return m_BlueTeam;

			case BlueTeam:
				return m_RedTeam;

			case Neutral:
				return null;

			default:
				return null;
		}
	}
	
	public void Wipe()
	{
		for(Iterator<Vehicle> Iter = m_Vehicles.iterator(); Iter.hasNext();)	
		{
			RemoveVehicle(Iter.next());
			Iter.remove();
		}
	}
		
	public Runner GetPlayer()
	{
		return m_Player;
	}
	
	public Vector<Vehicle> GetVehicles()
	{
		return m_Vehicles;
	}
	
	private class StartGameSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			m_MaxSpawnDistance = PlayingSpawnDistance;
		}	
	}
	
	private class EndGameSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			m_MaxSpawnDistance = ExhibitionSpawnDistance;
			
			m_Player = null;
		}	
	}
}