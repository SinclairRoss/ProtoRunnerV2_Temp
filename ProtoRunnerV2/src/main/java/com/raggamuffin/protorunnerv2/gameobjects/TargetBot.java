package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.ai.NavigationalBehaviourInfo;
import com.raggamuffin.protorunnerv2.ai.TargetingBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

public class TargetBot extends Vehicle
{
    private AIController m_AIController;

    public TargetBot(GameLogic game, Vector3 position)
    {
        super(game, ModelType.Dummy, position, 4.0, 1, VehicleClass.StandardVehicle, true, PublishedTopics.EnemyDestroyed, AffiliationKey.RedTeam);

        double x = MathsHelper.RandomDouble(-10, 10);
        double z = MathsHelper.RandomDouble(-10, 10);
        SetPosition(x, 0, z);

        NavigationalBehaviourInfo navInfo = new NavigationalBehaviourInfo(0.4, 1.0, 0.7, 0.6);
        m_AIController = new AIController(this, game.GetVehicleManager(), game.GetBulletManager(), navInfo, AIBehaviours.EngageTarget, FireControlBehaviour.Telegraphed, TargetingBehaviour.Standard);

        SetColour(Colours.CalvinOrange);

        m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(0);//2
        m_Engine.SetMaxEngineOutput(0);
        m_Engine.SetAfterBurnerOutput(0);
        m_Engine.SetDodgeOutput(0);

        SelectWeapon(new Weapon_None(this, game));

        OpenFire();
    }

    @Override
    public void Update(double deltaTime)
    {
        m_AIController.Update(deltaTime);

        super.Update(deltaTime);
    }
}
