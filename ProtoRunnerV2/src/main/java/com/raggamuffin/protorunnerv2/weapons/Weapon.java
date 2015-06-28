// Author:	Sinclair Ross.
// Date:	22/10/2014.
// Notes: 	This class represents a weapon. Extend this class to create new weapons.
//			Subclasses must define where the weapons muzzles are, otherwise there will be an array index out of bounds exception.

package com.raggamuffin.protorunnerv2.weapons;

import java.util.Vector;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.PostFireAction;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class Weapon 
{
	protected Vehicle m_Anchor;
	protected BulletManager m_BulletManager;
	protected ParticleManager m_ParticleManager;
	protected GameAudioManager m_AudioService;
		
	protected AudioClips m_AudioClip;
	
	protected FireControl m_FireMode;
	protected ProjectileTemplate m_ProjectileTemplate;

    protected String m_Name;

	protected double m_Damage;
    protected double m_Drain;
	protected double m_MuzzleVelocity;
	protected double m_Accuracy;
	protected double m_LifeSpan;
	protected double m_ProjectileFadeInTime;

	private Colour m_Colour;
	private Colour m_AltColour;
	
	private Vector<Vector3> m_MuzzleOffsets;
	private Vector3 m_MuzzleOffset;
	private int m_MuzzleIndex;
	
	protected boolean m_HasLasers;
	private Vector<LaserPointer> m_Lasers;
	
	protected PostFireAction m_PostFireAction;
	
	private boolean m_TriggerPulled;
    private boolean m_IsFiring;

	public Weapon(Vehicle anchor, BulletManager bManager, ParticleManager pManager, GameAudioManager audio)
	{
		m_Anchor = anchor;
		m_BulletManager = bManager;
		m_ParticleManager = pManager;
		m_AudioService = audio;
		
		m_AudioClip = AudioClips.PulseLaser;
		
		m_FireMode = null;
		m_ProjectileTemplate = null;

        m_Name = "---";

		m_Damage = 1.0;
        m_Drain = 1.0;
		m_MuzzleVelocity = 1.0;
		m_Accuracy = 1.0;
		m_LifeSpan = 1.0;
		m_ProjectileFadeInTime = 0.25;

		m_Colour = new Colour(Colours.White);
		m_AltColour = new Colour(Colours.White);
		
		m_MuzzleOffsets = new Vector<Vector3>();
		m_MuzzleOffset = new Vector3();
		m_MuzzleIndex = 0;
		m_HasLasers = false;
		m_Lasers = new Vector<LaserPointer>();

		m_PostFireAction = m_Anchor.GetPostFireAction();
		
		m_TriggerPulled = false;
        m_IsFiring = false;
	}
	
	// Override to add functionality.
	public void Update(double DeltaTime)
	{
        m_IsFiring = false;

        m_FireMode.Update(DeltaTime);
		
		if(m_FireMode.ShouldFire())
		{
			Fire();
			NextMuzzle();
			
			m_AudioService.PlaySound(m_Anchor.GetPosition(), m_AudioClip);
		}
	}
	
	public void OpenFire()
	{	
		m_FireMode.PullTrigger();
		m_TriggerPulled = true;
	}
	
	public void CeaseFire()
	{
		m_FireMode.ReleaseTrigger();
		m_TriggerPulled = false;
	}
	
	public Vector3 GetFirePosition()
	{
		m_MuzzleOffset.SetVector(m_MuzzleOffsets.elementAt(m_MuzzleIndex));

        m_MuzzleOffset.RotateY(m_Anchor.GetOrientation());
		m_MuzzleOffset.Add(m_Anchor.GetPosition());
		
		return m_MuzzleOffset;
	}
	
	public Vector3 GetMuzzlePosition(Vector3 Muzzle)
	{
		m_MuzzleOffset.SetVector(Muzzle);
		
		m_MuzzleOffset.RotateY(m_Anchor.GetOrientation());
		m_MuzzleOffset.Add(m_Anchor.GetPosition());
		
		return m_MuzzleOffset;
	}
	
	protected void NextMuzzle()
	{
		m_MuzzleIndex ++;
		
		if(m_MuzzleIndex >= m_MuzzleOffsets.size())
			m_MuzzleIndex = 0;
	}

	public Vector3 GetMuzzle()
	{
		return m_MuzzleOffsets.elementAt(m_MuzzleIndex);
	}
	
	protected void AddMuzzle(double I, double J, double K)
	{
		Vector3 Muzzle = new Vector3(I,J,K);
		m_MuzzleOffsets.add(Muzzle);
		
		if(m_HasLasers)
		{		
			LaserPointer Laser = new LaserPointer(this, Muzzle);
			m_Lasers.add(Laser);
		}
	}
	
	protected void Fire()
	{
		while(m_FireMode.ShouldFire())
		{
			m_BulletManager.CreateBullet(m_ProjectileTemplate);
			m_FireMode.NotifyOfFire();
			m_PostFireAction.Update();

            m_IsFiring = true;
		}
	}

	public void ResetMuzzleIndex()
	{
		m_MuzzleIndex = 0;
	}
	
	public void LasersOn()
	{
		for(LaserPointer Pointer : m_Lasers)
            Pointer.On();
	}
	
	public void LasersOff()
	{
		for(LaserPointer Pointer : m_Lasers)
			Pointer.Off();
	}
	
	protected void SetColour(double[] Colour)
	{
		m_Colour.SetColour(Colour);
		m_AltColour.SetAsInverse(m_Colour);
	}

	public Colour GetColour()
	{
		return m_Colour;
	}
	
	public Colour GetBulletColour()
	{
		return m_AltColour;
	}

	public Vector3 GetVelocity()
	{
		return m_Anchor.GetVelocity();
	}
	
	public Vector3 GetForward()
	{
		return m_Anchor.GetForward();
	}
	
	public Vector3 GetPosition()
	{
		return m_Anchor.GetPosition();
	}

	public double GetAccuracy()
	{
		return m_Accuracy;
	}

	public AffiliationKey GetAffiliation()
	{
		return m_Anchor.GetAffiliation();
	}

	public void AddChild(GameObject obj)
	{
		m_Anchor.AddChild(obj);
	}
	
	public double GetOrientation()
	{
		return m_Anchor.GetOrientation();
	} 
	
	public int GetNumMuzzles()
	{
		return m_MuzzleOffsets.size();
	}
	
	public Colour GetAltColour()
	{
		return m_AltColour;
	}
	
	public Vehicle GetAnchor()
	{
		return m_Anchor;
	}
	
	public boolean IsTriggerPulled()
	{
		return m_TriggerPulled;
	}
	
	public int GetMuzzleIndex()
	{
		return m_MuzzleIndex;
	}

    public double GetDrain()
    {
        return m_Drain;
    }

    public String GetName()
    {
        return m_Name;
    }

    public Boolean IsFiring()
    {
        return m_IsFiring;
    }
}