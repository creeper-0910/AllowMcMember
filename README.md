# Allow Minecraft Member
これはDiscordSRVと連携し、Discordに参加しているユーザーのみのログインを許可するSpigotプラグインです。  
あまりテストされていないため、問題が発生する可能性があります。
## 依存関係
DiscordSRVが必要です。
## インストール
DiscordSRVとAllowMcMemberをpluginフォルダに追加します。  
一度起動し、生成された`plugin/AllowMcMember/config.yml`内の項目を編集します。
内容は以下の通りです。  
* KickMessage
    * 許可されていないロールのユーザーが参加した場合のメッセージ
* linkMessage
    * リンクされていないロールのユーザーが参加した場合のメッセージ
* AllowUser
    * 許可するユーザーのロールID[^1]
[^1]: DIscordの設定→詳細設定→開発者モードをオンにし、Botを導入したサーバーのサーバー設定→ロール→許可したいユーザーの持つ最上位のロール名を右クリック→ロールIDをコピー
* RestrictionDay
    * 制限する曜日、削除した曜日は制限の対象外となり、すべてのユーザーが参加することが出来る
* Hours
    * 日付の切り替え(時)
* Minutes
    * 日付の切り替え(分)
## バグ報告
プラグインを書いたのはこれが初めてなので、バグがあるかもしれません。
報告や修正はこのリポジトリのissue、Pull requestにお願いします。