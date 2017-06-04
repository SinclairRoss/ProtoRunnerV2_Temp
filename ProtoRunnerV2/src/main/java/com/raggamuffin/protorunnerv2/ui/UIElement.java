package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Vector2;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class UIElement 
{
    protected final double SCREEN_RATIO;

    private UIElementType m_ElementType;

    private Vector2 m_Position;
    private Vector2 m_Scale;
    private Colour m_Colour;

	private double m_Rotation;
	
	private boolean m_Hidden;

	public UIElement(UIElementType elementType, UIManager uiManager)
	{
        SCREEN_RATIO = uiManager.GetScreenRatio();

        m_ElementType = elementType;

		m_Position = new Vector2();
		m_Scale = new Vector2(1);
        m_Colour = new Colour(Colours.White);

		m_Hidden = false;
	}

	public abstract void Update(double deltaTime);

	public void Hide()
	{
        m_Hidden = true;
	}
	public void Show() { m_Hidden = false; }

	public Vector2 GetPosition() { return m_Position; }
	public void SetPosition(double x, double y) { m_Position.SetVector(x * SCREEN_RATIO, y); }
    public void SetPosition(Vector2 pos) { m_Position.SetVector(pos); }

	public Vector2 GetScale() { return m_Scale; }
	public void SetScale(double x, double y) { m_Scale.SetVector(x, y); }
    public void SetScale(double scale) { m_Scale.SetVector(scale); }

	public Colour GetColour() { return m_Colour; }

    public void SetColour(Colour colour) { m_Colour.SetColour(colour); }
	public void SetColour(double[] colour) { m_Colour.SetColour(colour); }
    public void SetColour(Colour colour, double alpha) { m_Colour.SetColour(colour.Red, colour.Green, colour.Blue, alpha); }
	public void SetAlpha(double alpha) { m_Colour.Alpha = alpha; }

	public boolean IsHidden() { return m_Hidden; }
	public UIElementType GetType() { return m_ElementType; }

    public double GetRotation() { return m_Rotation; }
    public void SetRotation(double rotation) { m_Rotation = rotation; }
    public void Rotate(double rotation) { m_Rotation = ((m_Rotation + rotation) % (Math.PI * 2)); }

}
