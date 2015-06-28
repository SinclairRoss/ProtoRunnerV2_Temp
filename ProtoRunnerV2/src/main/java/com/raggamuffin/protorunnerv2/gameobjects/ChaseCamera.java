package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ChaseCamera
{
	///// Camera Attributes \\\\\
	private Vector3 m_Position;			// Position of the camera.
	private Vector3 m_Up;				// The up vector of the camera;
	private Vector3 m_LookAt;			// Where the camera is looking.
	
	private Vector3 m_PositionOffset;	// Position of the springs relaxed state relative to the Chase Object.
	private Vector3 m_RelaxedPosition;	// The Position of the Relaxed Position.
	private Vector3 m_Acceleration;		// The acceleration of the Camera.
	private Vector3 m_Velocity;			// The velocity of the camera.

	///// Chase Attributes \\\\\
	GameObject m_ChaseObject;			// The Chase Object.
	private Vector3 m_ChasePosition;	// The position of the Chase Object.
	private Vector3 m_ChaseForward;		// The Forward vector of the Chase Object.
	private Vector3 m_ChaseUp;			// The Up vector of the Chase Object.

	///// Spring Attributes \\\\\
	private Vector3 m_Stretch;			// The displacement of the springs current position from the resting position.
	private Vector3 m_Force;			// The force that is being exerted displacing the spring from it's resting position to its current position.
	
	private double m_Stiffness;			// The stiffness of the spring.
	private double m_Damping;			// The force acting against the spring.
	private double m_Mass;				// The Mass of the camera.

	public ChaseCamera()
	{
		///// Camera Attributes \\\\\
		m_Position 			= new Vector3(0.0,  3.0, 5.0);
		m_Up 				= new Vector3(0.0,  1.0,  0.0);
		m_LookAt 			= new Vector3(0.0);
		m_RelaxedPosition 	= new Vector3(0.0);		
		m_PositionOffset 	= new Vector3(-5.0, 5.0, -5.0);
		m_Acceleration 		= new Vector3(0.0);		
		m_Velocity 			= new Vector3(0.0);
		
		///// Chase Attributes \\\\\
		m_ChasePosition = new Vector3();
		m_ChaseForward  = new Vector3(0.0, 0.0, 1.0);
		m_ChaseUp 		= new Vector3(0.0, 1.0, 0.0);
		
		///// Spring Attributes \\\\\
		m_Stretch = new Vector3();
		m_Force = new Vector3();
		
		m_Stiffness = 0.08;
		m_Damping 	= 0.03;
		m_Mass  	= 0.5;	
	}
	
	public void Attach(GameObject ChaseObject)
	{
		m_ChaseObject = ChaseObject;
	}
	
	public void Update(double deltaTime)
	{
		m_ChasePosition.SetVector(m_ChaseObject.GetPosition());
		m_ChaseForward.SetVector(m_ChaseObject.GetForward());
		m_Up.SetVector(m_ChaseObject.GetUp());
		
		CalculateLookAt();
		
		CalculateRelaxedPosition();
		CalculateSpringForce();
		
		CalculateAcceleration();
		CalculateVelocity();
		UpdatePosition(deltaTime);
	}
	
	private void CalculateLookAt()
	{
		m_LookAt.I = m_ChasePosition.I + m_ChaseObject.GetForward().I * 10.0;
		m_LookAt.J = m_ChasePosition.J + m_ChaseObject.GetForward().J * 10.0;
		m_LookAt.K = m_ChasePosition.K + m_ChaseObject.GetForward().K * 10.0;
	}
	
	private void CalculateRelaxedPosition()
	{
		m_RelaxedPosition.I = m_ChasePosition.I + (m_ChaseForward.I * m_PositionOffset.I);
		m_RelaxedPosition.J = m_ChasePosition.J + (m_ChaseUp.J * 	  m_PositionOffset.J);
		m_RelaxedPosition.K = m_ChasePosition.K + (m_ChaseForward.K * m_PositionOffset.K);
	}
	
	private void CalculateSpringForce()
	{
		m_Stretch.I = m_Position.I - m_RelaxedPosition.I;
		m_Stretch.J = m_Position.J - m_RelaxedPosition.J;
		m_Stretch.K = m_Position.K - m_RelaxedPosition.K;
		
		// Find the force the spring is exerting in i, j & k using hooke's law.
		m_Force.I = (-m_Stiffness * m_Stretch.I) - (m_Damping * m_Velocity.I);
		m_Force.J = (-m_Stiffness * m_Stretch.J) - (m_Damping * m_Velocity.J);
		m_Force.K = (-m_Stiffness * m_Stretch.K) - (m_Damping * m_Velocity.K);	
	}
	
	private void CalculateAcceleration()
	{	
		m_Acceleration.I = (m_Force.I / m_Mass);
		m_Acceleration.J = (m_Force.J / m_Mass);
		m_Acceleration.K = (m_Force.K / m_Mass);
	}
	
	private void CalculateVelocity()
	{
		m_Velocity.I += m_Acceleration.I;
		m_Velocity.J += m_Acceleration.J;
		m_Velocity.K += m_Acceleration.K;
	}
	
	protected void UpdatePosition(double DeltaTime)
	{
		m_Position.I += m_Velocity.I * DeltaTime;
		m_Position.J += m_Velocity.J * DeltaTime;
		m_Position.K += m_Velocity.K * DeltaTime;
	}
	
	public void SetInPlace()
	{
		m_Position.SetVector(m_RelaxedPosition);
	}
	
	public Vector3 GetPosition()
	{
		return m_Position;
	}
	
	public Vector3 GetVelocity()
	{
		return m_Velocity;
	}
	
	public Vector3 GetLookAt()
	{
		return m_LookAt;
	}
	
	public Vector3 GetUp()
	{
		return m_Up;
	}
}































