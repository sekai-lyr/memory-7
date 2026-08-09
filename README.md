# 馃柤锔?Sekai Picture Wallpaper 路 浜屾鍏冨绾稿浘搴?
> **涓婁紶 路 鍒嗙被 路 绛涢€?路 鏀惰棌 路 鎵归噺鎿嶄綔 路 杩愯惀鐪嬫澘**
> A complete anime wallpaper gallery system

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![MyBatis](https://img.shields.io/badge/MyBatis-3-lightgrey)](https://mybatis.org/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3-005F0F)](https://www.thymeleaf.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8-4479A1)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

瀹屾暣鐨勪簩娆″厓澹佺焊鍥惧簱绯荤粺锛氫笂浼犮€佸垎绫汇€佺瓫閫夈€佹敹钘忋€佹壒閲忔搷浣滀笌杩愯惀鐪嬫澘涓€搴斾勘鍏紝鍙寘瑁呬负鍥惧簱绯荤粺婧愮爜 / 绉佹湁鍥惧簥 / 澹佺焊璧勬簮绔欏悗鍙般€?
A full-featured wallpaper gallery: upload, categorize, filter, favorite, batch operations & an ops dashboard.

## 鉁?鍔熻兘浜偣 Features

- 鐢ㄦ埛娉ㄥ唽銆佺櫥褰曘€侀€€鍑?User auth
- 澹佺焊鍒嗙被绠＄悊鍜屽垎绫荤瓫閫?Category management
- 鏍囬銆佽鑹层€佷綔鍝併€佹爣绛惧叧閿瘝鎼滅储 Keyword search
- 鍒嗛〉鏌ヨ銆佽壊璋冪瓫閫夈€佹瘮渚嬬瓫閫夈€佷笅杞介噺鎺掑簭 Filter & sort
- 涓婁紶鑷埗澹佺焊锛堜繚瀛樺埌 `uploads/wallpapers`锛塙pload
- 鏀惰棌/鍙栨秷鏀惰棌 Favorites
- 涓€閿笅杞藉拰涓嬭浇娆℃暟缁熻 Downloads tracking
- 鐑棬澹佺焊鎺ㄨ崘銆侀殢鏈烘娊鍙?Hot & random picks
- **鎵归噺鏀惰棌銆佹壒閲忓彇娑堟敹钘忋€佹壒閲忕Щ鍔ㄥ垎绫?* Batch operations
- 鍗＄墖鍐呭揩閫熺紪杈戞爣棰樸€佽鑹层€佹潵婧愩€佹爣绛俱€佽壊璋冨拰鍒嗙被 Quick edit
- 浼氬憳涓績婕旂ず锛氬椁愩€佷笅杞介搴︺€佸晢涓氬寲璺緞 Membership demo
- **杩愯惀鐪嬫澘**锛氳祫浜ц瘎鍒嗐€佹敹鍏ユā鍨嬨€佸師鍒涘崰姣斻€佹敹钘忚浆鍖栥€佺儹闂ㄦ Ops dashboard

## 馃挵 鍙樼幇鏂瑰悜 Monetization

鏇撮€傚悎鍖呰鎴?鍥惧簱绯荤粺婧愮爜 / 绉佹湁鍥惧簥 / 澹佺焊璧勬簮绔欏悗鍙?/ 鎺ュ崟灞曠ず椤圭洰"锛屼笉寤鸿鐩存帴鍞崠鏈巿鏉冨姩婕浘鐗囥€?
- 鍗栨簮鐮侊細鏁版嵁搴撹剼鏈€侀儴缃叉暀绋嬨€佹紨绀鸿处鍙枫€佷簩寮€鏈嶅姟
- 鎺ュ畾鍒讹細浼佷笟绱犳潗搴撱€佷釜浜虹浉鍐屻€丆OS 鍥惧簱銆佹憚褰变綔鍝佺珯
- 鍋氫細鍛樻紨绀猴細鎵╁睍 VIP 涓嬭浇棰濆害銆侀珮娓呭寘銆佹壒閲忔墦鍖呬笅杞?- 鍋氫綔鍝侀泦锛氬睍绀烘枃浠朵笂浼犮€佸浘鐗囩鐞嗐€佸垎椤垫煡璇€佺瓫閫夈€佹壒閲忔搷浣?
## 馃梽锔?鏁版嵁搴?Database

鍒濆鍖栬剼鏈湪 `build-data/init.sql`锛?
```powershell
mysql -uroot -p123456 --default-character-set=utf8mb4 -D sekai_friend < build-data/init.sql
```

## 鈻讹笍 鍚姩 Run

榛樿绔彛 `8087`锛?
```powershell
mvn spring-boot:run
```

璁块棶 `http://localhost:8087/login`銆?
## 鈿狅笍 鍟嗙敤鎻愰啋 Legal Note

鍏紑涓婄嚎鎴栨敹璐瑰墠锛岃浼樺厛浣跨敤鑷埗銆佹巿鏉冦€丆C0銆佸彲鍟嗙敤绱犳潗锛屾垨璁╃敤鎴疯嚜琛屼笂浼犲苟纭鐗堟潈銆傞」鐩湰韬彲浠ュ晢鐢ㄤ氦浠橈紝浣嗗浘鐗囩礌鏉愭槸鍚﹀彲鍟嗙敤闇€鍗曠嫭纭銆?
## 馃搫 License

[MIT](LICENSE) 漏 2026 [sekai-lyr](https://github.com/sekai-lyr)


<p align="center">
  <img src="screenshots/demo.webp" alt="Demo" width="720"/>
</p>
---

**猸?If this project helped you, star it! 濡傛灉杩欎釜椤圭洰瀵逛綘鏈夊府鍔╋紝娆㈣繋 Star锛?*
