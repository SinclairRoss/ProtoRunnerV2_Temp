package com.raggamuffin.protorunnerv2.managers;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

import com.raggamuffin.protorunnerv2.gameobjects.Drone;
import com.raggamuffin.protorunnerv2.gameobjects.Tank;
import com.raggamuffin.protorunnerv2.gameobjects.TargetBot;
import com.raggamuffin.protorunnerv2.gameobjects.VehicleType;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Bit;
import com.raggamuffin.protorunnerv2.gameobjects.Carrier;
import com.raggamuffin.protorunnerv2.gameobjects.Dummy;
import com.raggamuffin.protorunnerv2.gameobjects.Runner;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.gameobjects.WeaponTestBot;
import com.raggamuffin.protorunnerv2.gameobjects.Wingman;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class VehicleManager
{	
	private Runner m_Player;
	private ArrayList<Vehicle> m_Vehicles;
	private ArrayList<Vehicle> m_BlueTeam;
	private ArrayList<Vehicle> m_RedTeam;
	
	private GameLogic m_Game;
	
	private Publisher m_PlayerSpawnedPublisher;
	
	public VehicleManager(GameLogic Game)
	{
		m_Game = Game;

		m_Vehicles = new ArrayList<>();
		m_BlueTeam = new ArrayList<>();
		m_RedTeam  = new ArrayList<>();

		PubSubHub PubSub = m_Game.GetPubSubHub();
		
		m_PlayerSpawnedPublisher = PubSub.CreatePublisher(PublishedTopics.PlayerSpawned);
	}
	
	public void Update(double deltaTime)
	{
        for(int i = 0; i < m_Vehicles.size(); i++)
        {
            Vehicle object = m_Vehicles.get(i);

            if(object.IsValid())
            {
                object.Update(deltaTime);
            }
            else
            {
                RemoveVehicle(object);
                m_Vehicles.remove(object);
                i--;
            }
        }
	}

	// Removes vehicle from their team and the renderer.
	// DOES NOT remove vehicle from m_Vehicles.
	private void RemoveVehicle(Vehicle object)
	{		
		m_Game.RemoveGameObjectFromRenderer(object);
		GetTeam(object.GetAffiliation()).remove(object);
		
		if(object == m_Player)
			m_Player = null;
	}

	public void SpawnPlayer()
	{
		// Ensures no more than one player is active at any one a time.
		if(m_Player != null)
			return;
		
		Log.e("Player", "Player spawn.");
		
		m_Player = new Runner(m_Game);
		m_Vehicles.add(m_Player);
		m_BlueTeam.add(m_Player);
		m_Game.AddObjectToRenderer(m_Player);

		m_PlayerSpawnedPublisher.Publish();
	}

	public void SpawnWingmen(int i)
	{
		Wingman buddy = new Wingman(m_Game);

        if(m_Player != null)
        {
            buddy.SetYaw(m_Player.GetYaw());
        }

        Vector3 pos = new Vector3(i, 0, 0);
        buddy.SetPosition(pos);

		m_Vehicles.add(buddy);
		m_BlueTeam.add(buddy);
		m_Game.AddObjectToRenderer(buddy);
	}

    public void SpawnDummy(double x, double z)
    {
        Dummy dummy = new Dummy(m_Game, x, z);
        m_Vehicles.add(dummy);
        m_RedTeam.add(dummy);
        m_Game.AddObjectToRenderer(dummy);
    }

    public Vehicle SpawnVehicle(VehicleType type, Vector3 spawnPoint)
    {
        Vehicle spawn = null;

        switch(type)
        {
            case Wingman:
                spawn = new Wingman(m_Game);
                break;
            case Bit:
                spawn = new Bit(m_Game);
                break;
            case Tank:
                spawn = new Tank(m_Game);
                break;
            case Carrier:
                spawn = new Carrier(m_Game);
                break;
            case WeaponTestBot:
                spawn = new WeaponTestBot(m_Game);
                break;
            case TargetBot:
                spawn = new TargetBot(m_Game);
                break;
        }

        spawn.SetPosition(spawnPoint);
        m_Vehicles.add(spawn);
        GetTeam(spawn.GetAffiliation()).add(spawn);
        m_Game.AddObjectToRenderer(spawn);

        return spawn;
    }

    public Drone SpawnDrone(Carrier anchor, Vector3 pos)
    {
        Drone spawn = new Drone(m_Game, anchor);
        spawn.SetPosition(pos.I, 4.0, pos.K);

        m_Vehicles.add(spawn);
        GetTeam(spawn.GetAffiliation()).add(spawn);
        m_Game.AddObjectToRenderer(spawn);

        return spawn;
    }
	
	public void SpawnSquad(double maxSpawnDistance)
	{
		double I = MathsHelper.RandomDouble(-1.0, 1.0);
		double J = 0;
		double K = MathsHelper.RandomDouble(-1.0, 1.0);
		
		Vector3 m_Spawn = new Vector3(I,J,K);
		m_Spawn.Normalise();
		m_Spawn.Scale(maxSpawnDistance);
		
		if(m_Player != null)
			m_Spawn.Add(m_Player.GetPosition());

        Carrier tank = new Carrier(m_Game);
        m_Vehicles.add(tank);
        m_RedTeam.add(tank);
        m_Game.AddObjectToRenderer(tank);

        tank.SetPosition(m_Spawn.I, m_Spawn.J, m_Spawn.K);

        for(int b = 0; b < 6; b++)
        {
            Bit bit = new Bit(m_Game);
            m_Vehicles.add(bit);
            m_RedTeam.add(bit);
            m_Game.AddObjectToRenderer(bit);

            bit.SetPosition(m_Spawn.I + b * 2,m_Spawn.J,m_Spawn.K);
        }
	}
	
	public ArrayList<Vehicle> GetTeam(AffiliationKey faction)
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
	
	public ArrayList<Vehicle> GetOpposingTeam(AffiliationKey faction)
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

    public int GetTeamCount(AffiliationKey faction)
    {
        switch(faction)
        {
            case BlueTeam:
                return m_BlueTeam.size();
            case RedTeam:
                return m_RedTeam.size();
            default:
                return 0;
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
	
	public ArrayList<Vehicle> GetVehicles()
	{
		return m_Vehicles;
	}
}
