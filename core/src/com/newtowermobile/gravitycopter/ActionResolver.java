package com.newtowermobile.gravitycopter;

public interface ActionResolver {
  boolean getSignedInGPGS();
  void loginGPGS();
  void submitScoreGPGS(int score);
  void unlockAchievementGPGS(String achievementId);
  void getLeaderboardGPGS();
  void getAchievementsGPGS();

  void showAds(boolean show);
}