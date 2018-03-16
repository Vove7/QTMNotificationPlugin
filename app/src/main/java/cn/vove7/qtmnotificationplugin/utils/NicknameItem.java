package cn.vove7.qtmnotificationplugin.utils;

public class NicknameItem {
   private String nickname;
   private boolean status;

   public NicknameItem(String nickname, boolean status) {
      this.nickname = nickname;
      this.status = status;
   }

   public boolean isStatus() {
      return status;
   }

   public void setStatus(boolean status) {
      this.status = status;
   }

   public String getNickname() {
      return nickname;
   }

   public void setNickname(String nickname) {
      this.nickname = nickname;
   }
}
