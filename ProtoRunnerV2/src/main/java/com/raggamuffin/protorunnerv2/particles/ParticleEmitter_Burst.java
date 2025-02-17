package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ParticleEmitter_Burst extends ParticleEmitter
{
    private int m_EmissionCount;
    private Vector3 m_ParticleForward;

    public ParticleEmitter_Burst(GameLogic game, Colour initialColour, Colour finalColour)
    {
        super(game, initialColour, finalColour, 60, 3);

        m_EmissionCount = 150;
        m_ParticleForward = new Vector3();

        SetForward(0,1,0);
    }

    public void Burst()
    {
        for (int i = 0; i < m_EmissionCount; ++i)
        {
            CreateParticle();
        }
    }

    @Override
    public Vector3 CalculateParticleForward()
    {
        double i = MathsHelper.RandomDouble(-1.0, 1.0);
        double j = MathsHelper.RandomDouble(-1.0, 1.0);
        double k = MathsHelper.RandomDouble(-1.0, 1.0);

        m_ParticleForward.SetVector(i, j, k);
        m_ParticleForward.Normalise();

        return m_ParticleForward;
    }
}