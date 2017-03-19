package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   17/08/2016

import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class Shield extends GameObject
{
    private final double GROWTH_RATE = 3.0;

    private double m_TargetScale;
    protected double m_NormalisedScale;
    private double m_GrowthRate;
    private Vehicle m_Target;
    private Vehicle m_Anchor;

    public Shield(Vehicle anchor)
    {
        super(ModelType.Shield, anchor.GetBoundingRadius());

        SetScale(0);

        m_TargetScale = 1.0;
        m_NormalisedScale = 0.0;
        m_GrowthRate = 0.0;
        m_Anchor = anchor;

        m_Target = null;
    }

    public void AttachToObject(Vehicle object)
    {
        SetColour(object.GetColour());
        m_TargetScale = object.GetBoundingRadius() * 1.5;
        m_GrowthRate = GROWTH_RATE;

        m_Target = object;
        m_Target.ApplyStatusEffect(StatusEffect.Shielded);
    }

    public void DetachFromObject()
    {
        m_GrowthRate = -GROWTH_RATE;

        if(m_Target != null)
        {
            m_Target.RemoveStatusEffect(StatusEffect.Shielded);
            m_Target = null;
        }
    }

    @Override
    public void Update(double deltaTime)
    {
        SetPosition(m_Anchor.GetPosition());

        m_NormalisedScale += m_GrowthRate * deltaTime;
        m_NormalisedScale = MathsHelper.Clamp(m_NormalisedScale, 0, 1);

        double horizontalScale = MathsHelper.Lerp(m_NormalisedScale, 0, m_TargetScale);
        double verticalScale = horizontalScale;
        SetScale(horizontalScale, verticalScale, horizontalScale);

        if(m_Target != null)
        {
            SetForward(m_Target.GetForward());
        }
    }

    @Override
    public boolean IsValid()
    {
        return m_Anchor.IsValid() || m_NormalisedScale > 0;
    }

    @Override
    public void CleanUp()
    {
        DetachFromObject();
    }
}
