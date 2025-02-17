package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   12/02/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Quaternion;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class GameObject
{
    public boolean DEBUG_BOUNDINGRING = false;

    private Vector3 m_Position;
    private Vector3 m_PreviousPosition;
    private Vector3 m_Velocity;
    private Vector3 m_Scale;

    private Vector3 m_Up;
    private Vector3 m_Forward;
    private Vector3 m_Right;

    private ModelType m_Model;
    private Colour m_Colour;
    private double m_InnerColourItensity;

    private double m_BoundingRadius;
    private double m_DragCoefficient;

    public GameObject(ModelType model, double boundingRadius)
    {
        m_Position = new Vector3(0);
        m_PreviousPosition = new Vector3();
        m_Velocity = new Vector3();
        m_Scale = new Vector3(1);

        m_Up = new Vector3(Vector3.UP);
        m_Forward = new Vector3(Vector3.FORWARD);
        m_Right = new Vector3(Vector3.RIGHT);

        m_Model = model;
        m_Colour = new Colour(Colours.White);
        m_InnerColourItensity = 0;

        m_BoundingRadius = boundingRadius;

        m_DragCoefficient = 5;
    }

    public void Update(double deltaTime)
    {
        m_Forward.Normalise();
        m_Right.Normalise();
        m_Up.Normalise();

        ApplyDrag(deltaTime);
        m_PreviousPosition.SetVector(GetPosition());
        Translate(m_Velocity.X * deltaTime, m_Velocity.Y * deltaTime, m_Velocity.Z * deltaTime);
    }

    private void ApplyDrag(double deltaTime)
    {
        double drag_x = (m_DragCoefficient * m_Velocity.X);
        double drag_y = (m_DragCoefficient * m_Velocity.Y);
        double drag_z = (m_DragCoefficient * m_Velocity.Z);

        ApplyForce(-drag_x, -drag_y, -drag_z, deltaTime);
    }

    public boolean IsValid()
    {
        return true;
    }

    public void CleanUp()
    {}

    // <----- Forces -----> \\
    public void ApplyForce(Vector3 force, double scalar)
    {
        ApplyForce(force.X, force.Y, force.Z, scalar);
    }

    public void ApplyForce(Vector3 direction, double force, double scalar)
    {
        ApplyForce(direction.X * force, direction.Y * force, direction.Z * force, scalar);
    }

    public void ApplyForce(double force_x, double force_y, double force_z, double scalar)
    {
        m_Velocity.X += force_x * scalar;
        m_Velocity.Y += force_y * scalar;
        m_Velocity.Z += force_z * scalar;
    }

    // <----- Getters -----> \\
    public Vector3 GetPosition()
    {
        return m_Position;
    }

    public Vector3 GetPreviousPosition() { return m_PreviousPosition; }
    public Vector3 GetVelocity() { return m_Velocity; }
    public Vector3 GetScale() { return m_Scale; }
    public Vector3 GetUp() { return m_Up; }
    public Vector3 GetForward() { return m_Forward; }
    public Vector3 GetRight() { return m_Right; }

    public void Rotate(Quaternion a)
    {
        m_Right.Multiply(a);
        m_Up.Multiply(a);
        m_Forward.Multiply(a);
    }

    public void SetRotation(Quaternion a)
    {
        m_Forward.SetVector(Vector3.FORWARD);
        m_Right.SetVector(Vector3.RIGHT);
        m_Up.SetVector(Vector3.UP);

        Rotate(a);
    }

    public ModelType GetModel()
    {
        return m_Model;
    }

    public Colour GetColour()
    {
        return m_Colour;
    }

    public double GetInnerColourIntensity()
    {
        return m_InnerColourItensity;
    }

    public double GetBoundingRadius()
    {
        return m_BoundingRadius;
    }

    // <----- Setters -----> \\
    public void SetPositionByRef(Vector3 position)
    {
        m_Position = position;
    }

    public void SetPosition(Vector3 position)
    {
        SetPosition(position.X, position.Y, position.Z);
    }

    public void SetPosition(double x, double y, double z)
    {
        m_Position.SetVector(x, y, z);
        m_PreviousPosition.SetVector(x, y, z);
    }

    protected void SetVelocity(Vector3 direction, double scalar)
    {
        m_Velocity.SetVector(direction.X * scalar, direction.Y * scalar, direction.Z * scalar);
    }

    protected void SetVelocity(Vector3 velocity)
    {
        m_Velocity.SetVector(velocity);
    }

    protected void SetVelocity(double scale)
    {
        m_Velocity.SetVector(scale);
    }

    public void Translate(Vector3 translation)
    {
        m_Position.Add(translation);
    }

    public void Translate(double x, double y, double z)
    {
        m_Position.Add(x, y, z);
    }

    public void SetScale(double x, double y, double z)
    {
        m_Scale.SetVector(x, y, z);
    }

    public void SetScale(double scale)
    {
        m_Scale.SetVector(scale);
    }

    protected void LookAt(Vector3 look)
    {
        m_Forward.SetVectorAsDifference(m_Position, look);
        m_Forward.Normalise();
        SetForward(m_Forward);
    }

    protected void UpdateOrientationVectorsWithForward()
    {
        SetForward(m_Forward);
    }

    public void SetRotationY(double yaw)
    {
        m_Forward.SetVector(Vector3.FORWARD);
        m_Forward.RotateY(yaw);
        SetForward(m_Forward);
    }

    public void SetForward(Vector3 forward)
    {
        SetForward(forward.X, forward.Y, forward.Z);
    }
    public void SetForward(double x, double y, double z)
    {
        m_Forward.SetVector(x, y ,z);

        Vector3 up = m_Forward.GetMajorComponent() != Vector3.Component.Y ? Vector3.UP : Vector3.FORWARD;

        m_Right.SetVectorAsCrossProduct(up, m_Forward);
        m_Up.SetVectorAsCrossProduct(m_Forward, m_Right);
    }

    protected void SetOrientationVectorsByRef(GameObject obj)
    {
        m_Forward = obj.m_Forward;
        m_Up = obj.m_Up;
        m_Right = obj.m_Right;
    }

    public void SetOrientationVectors(double fx, double fy, double fz,    double ux, double uy, double uz,    double rx, double ry, double rz)
    {
        m_Forward.SetVector(fx, fy, fz);
        m_Up.SetVector(ux, uy, uz);
        m_Right.SetVector(rx, ry, rz);
    }

    public void SetColourByRef(Colour colour) { m_Colour = colour; }
    public void SetColour(Colour colour) { m_Colour.SetColour(colour); }
    public void SetColour(double[] colour) { m_Colour.SetColour(colour); }
    public void SetAlpha(double alpha) { m_Colour.Alpha = alpha; }

    protected void SetDragCoefficient(double drag) { m_DragCoefficient = drag; }
}