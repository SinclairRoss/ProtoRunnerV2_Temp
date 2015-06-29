package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

public class Dummy extends Vehicle
{
    private AIController m_AIController;

    public Dummy(GameLogic game, double x, double z)
    {
        super(game);

        m_Model = ModelType.Dummy;
        SetAffiliation(AffiliationKey.RedTeam);

        m_OnDeathPublisher = game.GetPubSubHub().CreatePublisher(PublishedTopics.DummyDestroyed);

        m_Position.SetVector(x, 0, z);

        m_Engine.SetMaxEngineOutput(1500);
        m_Engine.SetAfterBurnerOutput(1);

        SelectWeapon(new Weapon_None(this, game));

        m_AIController = new AIController(this, game.GetVehicleManager(), game.GetBulletManager());
    }

    @Override
    public void Update(double deltaTime)
    {
        m_AIController.Update(deltaTime);
        super.Update(deltaTime);
    }
}