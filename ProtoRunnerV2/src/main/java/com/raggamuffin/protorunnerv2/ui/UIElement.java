package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Vector2;

public abstract class UIElement 
{
    protected final double SCREEN_RATIO;

	protected Vector2 m_Position;
	protected Vector2 m_OriginalPosition;
	protected Vector2 m_Size;
	
	private boolean m_Hidden;

	protected UIElementType m_Type;

	protected Colour m_Colour;
	
	public UIElement(UIManager uiManager)
	{
        SCREEN_RATIO = uiManager.GetScreenRatio();

		m_OriginalPosition = new Vector2(0,0);
		m_Position  = new Vector2(m_OriginalPosition);
		m_Size 		= new Vector2();

		m_Hidden = false;
		
		m_Type = UIElementType.Undefined;
		m_Colour = new Colour(Colours.White);
	}

	public abstract void Update(double DeltaTime);
	protected abstract void TriggerOpenAnimation(double delay);
    protected abstract void TriggerClosingAnimation();

	public void Hide()
	{
        TriggerClosingAnimation();
	}

	public void Show()
	{
		Show(0.0);
	}
	
	public void Show(double delay)
	{
		TriggerOpenAnimation(delay);
        SetHidden(false);
	}

    public void SetHidden(boolean hide)
    {
        m_Hidden = hide;
    }
	
	public void AlignLeft()
	{
		m_Position.I = m_OriginalPosition.I;
	}
	
	public void AlignRight()
	{
		m_Position.I = m_OriginalPosition.I - m_Size.I;
	}
	
	public void CentreHorizontal()
	{
		m_Position.I = m_OriginalPosition.I - (m_Size.I * 0.5);
	}

	public void SetPosition(double x, double y)
	{
        SetPositionRaw(x * SCREEN_RATIO, y);
	}

    public void SetPositionRaw(double x, double y)
    {
        m_OriginalPosition.SetVector(x, y);
        m_Position.SetVector(x,y);
    }

    public void SetSize(double width, double height)
    {
        m_Size.SetVector(width, height);
    }

	///// Getters.
	public Vector2 GetPosition()
	{
		return m_Position;
	}

	public Vector2 GetSize()
	{
		return m_Size;
	}
	
	public boolean IsHidden()
	{
		return m_Hidden;
	}

	public UIElementType GetType()
	{
		return m_Type;
	}

    public void SetColour(double[] newColour)
    {
        m_Colour.SetColour(newColour);
    }

    public void SetColour(Colour newColour)
    {
        SetColour(newColour);
    }
	
	public Colour GetColour()
	{
		return m_Colour;
	}
}
