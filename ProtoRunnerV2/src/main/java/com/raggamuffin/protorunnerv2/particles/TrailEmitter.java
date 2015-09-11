package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.utils.Colour;

public class TrailEmitter extends GameObject
{
    private final GameObject m_Anchor;
    private GameLogic m_Game;
    private final ParticleManager m_ParticleManager;

    private TrailPoint m_HeadNode;

    private final double m_LifeSpan;
    private final double m_EmissionRate;
    private double m_EmissionCounter;

    private Colour m_HotColour;
    private Colour m_ColdColour;

    public TrailEmitter(GameObject anchor, GameLogic game)
    {
        super(null, null);

        m_Anchor = anchor;
        m_Game = game;
        m_ParticleManager = m_Game.GetParticleManager();

        m_HeadNode = new TrailPoint();
        m_HeadNode.GetPosition().SetVector(m_Position);

        m_LifeSpan = 2.0;
        m_EmissionRate = 0.1;
        m_EmissionCounter = m_EmissionRate;

        m_HotColour = anchor.GetBaseColour();
        m_ColdColour = anchor.GetAltColour();
    }

    @Override
    public void Update(double deltaTime)
    {
        m_EmissionCounter += deltaTime;

        if(m_EmissionCounter >= m_EmissionRate)
        {
            m_EmissionCounter = 0;
            m_HeadNode = m_ParticleManager.CreateTrailPoint(this);
        }

        m_HeadNode.GetPosition().SetVector(m_Position);
        m_HeadNode.Update(deltaTime);
    }

    @Override
    public boolean IsValid()
    {
        return m_Anchor.IsValid();
    }

    public TrailPoint GetHeadNode()
    {
        return m_HeadNode;
    }

    public double GetLifeSpan()
    {
        return m_LifeSpan;
    }

    public Colour GetHotColour()
    {
        return m_HotColour;
    }

    public Colour GetColdColour()
    {
        return m_ColdColour;
    }
}
