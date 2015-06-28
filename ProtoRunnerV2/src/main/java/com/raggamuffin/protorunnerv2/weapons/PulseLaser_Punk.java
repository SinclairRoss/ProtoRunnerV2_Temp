package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class PulseLaser_Punk extends Weapon
{
	public PulseLaser_Punk(Vehicle anchor, VehicleManager vManager, BulletManager bManager, ParticleManager pManager, GameAudioManager audio, PubSubHub pubSub)
	{
		super(anchor, bManager, pManager, audio);
		
		m_Damage = 200;
		m_MuzzleVelocity = 0.6;
		m_Accuracy = 1.0;
		m_LifeSpan = 2.0;
		
		m_FireMode = new FireControl_Pulse(0.6, 0.06, 2);
		m_ProjectileTemplate = new ProjectileTemplate(this, m_Anchor.GetVehicleInfo(), ModelType.PulseLaser, GetAffiliation(),
														m_MuzzleVelocity, m_Damage, m_LifeSpan, m_ProjectileFadeInTime,
														ProjectileBehaviourType.Standard, m_ParticleManager, m_BulletManager, audio, vManager, pubSub);

		m_AudioClip = AudioClips.PulseLaser;
		
		m_HasLasers = false;
		
		AddMuzzle( 1, 0, 0);
		AddMuzzle(-1, 0, 0);
		
		SetColour(Colours.Crimson);
	}
}