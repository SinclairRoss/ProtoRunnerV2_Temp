package com.raggamuffin.protorunnerv2.master;

import android.content.Context;
import android.graphics.Point;

import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Tentacle;
import com.raggamuffin.protorunnerv2.particles.Particle;
import com.raggamuffin.protorunnerv2.particles.Trail;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UIElementType;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class RendererPacket
{
    private final Point m_ScreenSize;
    private final ArrayList<CopyOnWriteArrayList<GameObject>> m_GameObjects;
    private final CopyOnWriteArrayList<Trail> m_Trails;
    private final CopyOnWriteArrayList<Tentacle> m_Tentacles;
    private final CopyOnWriteArrayList<FloorGrid> m_FloorGrids;

    private final Object m_ParticleMutex;
    private final CopyOnWriteArrayList<Particle> m_Particles;

    private final ArrayList<CopyOnWriteArrayList<UIElement>> m_UIElements;
    private final ChaseCamera m_Camera;
    private final Context m_Context;
    private final RenderEffectSettings m_RenderEffectSettings;

    public RendererPacket(Context context, ChaseCamera camera, RenderEffectSettings settings, Point screenSize)
    {
        m_ScreenSize = screenSize;

        int numModels = ModelType.values().length;
        m_GameObjects = new ArrayList<>(numModels);
        for (int i = 0; i < numModels; ++i)
        {
            m_GameObjects.add(new CopyOnWriteArrayList<GameObject>());
        }

       int numUIElements = UIElementType.values().length;
       m_UIElements = new ArrayList<>(numUIElements);

       for (int i = 0; i < numUIElements; ++i)
       {
           m_UIElements.add(new CopyOnWriteArrayList<UIElement>());
       }

       m_ParticleMutex = new Object();
       m_Particles = new CopyOnWriteArrayList<>();
       m_Trails = new CopyOnWriteArrayList<>();
       m_Tentacles = new CopyOnWriteArrayList<>();
       m_FloorGrids = new CopyOnWriteArrayList<>();

        m_Context = context;
        m_Camera = camera;
        m_RenderEffectSettings = settings;
    }

    public void AddObject(Trail trail) { m_Trails.add(trail); }
    public void RemoveObject(Trail trail) { m_Trails.remove(trail); }
    public CopyOnWriteArrayList<Trail> GetTrails() { return m_Trails; }

    public void AddObject(Tentacle node) { m_Tentacles.add(node); }
    public void RemoveObject(Tentacle node) { m_Tentacles.remove(node);}
    public CopyOnWriteArrayList<Tentacle> GetRopes()
    {
        return m_Tentacles;
    }

    public void AddObject(GameObject object) { GetModelList_InGame(object.GetModel()).add(object); }
    public void RemoveObject(GameObject object) { GetModelList_InGame(object.GetModel()).remove(object); }

    public CopyOnWriteArrayList<GameObject> GetModelList_InGame(ModelType type) { return m_GameObjects.get(type.ordinal()); }

    public void AddObject(Particle particle) { m_Particles.add(particle); }
    public void AddObject(ArrayList<Particle> particles) { m_Particles.addAll(particles); }
    public void RemoveObject(Particle particle) { m_Particles.remove(particle); }
    public void RemoveObject(ArrayList<Particle> particles) { m_Particles.removeAll(particles); }
    public CopyOnWriteArrayList<Particle> GetParticles() { return m_Particles; }
    public Object GetParticleMutex() { return m_ParticleMutex; }

    public void AddObject(FloorGrid floorGrid) { m_FloorGrids.add(floorGrid); }
    public void RemoveObject(FloorGrid floorGrid) { m_FloorGrids.remove(floorGrid); }
    public CopyOnWriteArrayList<FloorGrid> GetFloorGrids()
    {
        return m_FloorGrids;
    }

    public void AddUIElement(UIElement element) { GetUIElementList(element.GetType()).add(element); }
    public void RemoveUIElement(UIElement element) { GetUIElementList(element.GetType()).remove(element); }
    public CopyOnWriteArrayList<UIElement> GetUIElementList(UIElementType type) { return m_UIElements.get(type.ordinal()); }

	public Context GetContext() { return m_Context; }
	public ChaseCamera GetCamera() { return m_Camera; }
	public RenderEffectSettings GetRenderEffectSettings() { return m_RenderEffectSettings; }
    public Point GetScreenSize() { return m_ScreenSize; }
}
