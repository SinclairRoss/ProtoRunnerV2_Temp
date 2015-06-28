package com.raggamuffin.protorunnerv2.ui;

import java.util.ArrayList;

import android.content.Context;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.data.HighScoreRow;
import com.raggamuffin.protorunnerv2.data.HighScoreTable;
import com.raggamuffin.protorunnerv2.data.TableRow;
import com.raggamuffin.protorunnerv2.data.TableType;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gamelogic.GameStats;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;

public class AftermathScreen extends UIScreen
{
	private UILabel m_Title;
	private UIButton m_Back;
	
	private UILabel m_ScoreLabel;
	private UILabel m_PlayTimeLabel;
	private UILabel m_TimesRebootedLabel;
	private UIProgressBar m_AccuracyBar;
	private UIProgressBar m_WingmanALifeBar;
	private UIProgressBar m_WingmanBLifeBar;
	
	public AftermathScreen(GameLogic Game, UIManager Manager) 
	{
		super(Game, Manager);

		m_Title = null;
		m_Back = null;
		
		m_ScoreLabel = null;
		m_PlayTimeLabel = null;
		m_TimesRebootedLabel = null;
		m_AccuracyBar = null;
		m_WingmanALifeBar = null;
		m_WingmanBLifeBar = null;
    }
	
	@Override
	public void Create()
	{
		super.Create();
		
		Context context = m_Game.GetContext();
		GameStats stats = m_Game.GetGameStats();
		
		m_Title = CreateTitle(context.getString(R.string.aftermath_screen_title));
		
		if(m_Game.GetGameStats().GetScore() > GetMinScore() && m_Game.GetGameStats().isRealData())
			m_Back = CreateBackButton(UIScreens.NewHighScore);
		else
			m_Back = CreateBackButton(UIScreens.HighScore);	
		
		m_ScoreLabel = CreateLabel(context.getString(R.string.label_score) + stats.GetScore());
		
		m_PlayTimeLabel = CreateLabel(context.getString(R.string.label_play_time) + stats.GetPlayTimeString());
		m_TimesRebootedLabel = CreateLabel(context.getString(R.string.label_num_reboots) + Integer.toString(stats.GetLivesUsed()));
		
		m_AccuracyBar = CreateProgressBar(context.getString(R.string.label_accuracy), 1.0);	
		m_AccuracyBar.SetValue(stats.GetAccuracy());	
		
		m_WingmanALifeBar = CreateProgressBar(context.getString(R.string.label_wingman_a_life), stats.GetPlayTime());	
		m_WingmanALifeBar.SetValue(stats.GetWingmanADuration());	
		
		m_WingmanBLifeBar = CreateProgressBar(context.getString(R.string.label_wingman_b_life), stats.GetPlayTime());	
		m_WingmanBLifeBar.SetValue(stats.GetWingmanBDuration());
	}
	
	@Override
	public void Remove() 
	{
		m_UIManager.RemoveUIElement(m_Title);
		m_Title = null;
		
		m_UIManager.RemoveUIElement(m_Back);
		m_Back = null;
		
		m_UIManager.RemoveUIElement(m_ScoreLabel);
		m_ScoreLabel = null;
		
		m_UIManager.RemoveUIElement(m_PlayTimeLabel);
		m_PlayTimeLabel = null;
		
		m_UIManager.RemoveUIElement(m_TimesRebootedLabel);
		m_TimesRebootedLabel = null;
		
		m_UIManager.RemoveUIElement(m_AccuracyBar);
		m_AccuracyBar = null;
		
		m_UIManager.RemoveUIElement(m_WingmanALifeBar);
		m_WingmanALifeBar = null;
		
		m_UIManager.RemoveUIElement(m_WingmanBLifeBar);
		m_WingmanBLifeBar = null;
		
		m_UIManager.RemoveUIElement(m_ScoreLabel);
		m_ScoreLabel = null;
	}

	@Override
	public void Update(double DeltaTime) 
	{
		ControlScheme Scheme = m_Game.GetControlScheme();
		
		if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_Back))
		{
			m_Back.Pressed();
			Scheme.ResetTouchCoordinates();
			return;
		}
	}
	
	private int GetMinScore()
	{
		ArrayList<TableRow> rows = m_Game.GetDatabaseManager().GetTableData(TableType.Highscore);

		if(rows.size() < HighScoreTable.MAX_HIGH_SCORES)
			return 0;
		
		int minScore = Integer.MAX_VALUE;

		for(TableRow row : rows)
		{
			HighScoreRow scoreRow = (HighScoreRow) row;
			
			int score = scoreRow.GetScore();
			
			if(score < minScore)
			{
				minScore = score;
			}
		}
		
		return minScore;
	}
}