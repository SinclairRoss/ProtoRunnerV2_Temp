package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
import com.raggamuffin.protorunnerv2.particles.TrailEmitterDepricated;
import com.raggamuffin.protorunnerv2.utils.Colour;

public class Engine_Standard extends Engine
{
    public Engine_Standard(GameObject anchor, GameLogic game, EngineUseBehaviour behaviour)
    {
        super(anchor, behaviour);

        m_Anchor.AddChild(new TrailEmitterDepricated(m_Anchor, game.GetParticleManager()));
        m_Anchor.AddChild(new TrailEmitter(m_Anchor, game));
    }

    @Override
    public void UpdateParticleColours(Colour start, Colour end)
    {
    //    m_TrailEmitterDepricated.SetStartColour(start);
     //   m_TrailEmitterDepricated.SetFinalColour(end);
    }
}