package com.xyh;

public enum AnnonationNote {
	
//	  ┌───┐   ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
//	  │Esc│   │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│  ┌┐    ┌┐    ┌┐
//	  └───┘   └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘  └┘    └┘    └┘
//	  ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐ ┌───┬───┬───┐ ┌───┬───┬───┬───┐
//	  │~ `│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp │ │Ins│Hom│PUp│ │N L│ / │ * │ - │
//	  ├───┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤ ├───┼───┼───┤ ├───┼───┼───┼───┤
//	  │ Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ │ │Del│End│PDn│ │ 7 │ 8 │ 9 │   │
//	  ├─────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤ └───┴───┴───┘ ├───┼───┼───┤ + │
//	  │ Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │               │ 4 │ 5 │ 6 │   │
//	  ├──────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤     ┌───┐     ├───┼───┼───┼───┤
//	  │ Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │     │ ↑ │     │ 1 │ 2 │ 3 │   │
//	  ├─────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤ ┌───┼───┼───┐ ├───┴───┼───┤ E││
//	  │ Ctrl│    │Alt │         Space         │ Alt│    │    │Ctrl│ │ ← │ ↓ │ → │ │   0   │ . │←─┘│
//	  └─────┴────┴────┴───────────────────────┴────┴────┴────┴────┘ └───┴───┴───┘ └───────┴───┴───┘
//
//	  Code is far away from bug with the keyboard protecting.
	
	


	/**
	*                            _ooOoo_
	*                           o8888888o
	*                           88" . "88
	*                           (| -_- |)
	*                            O\ = /O
	*                        ____/`---'\____
	*                      .   ' \\| |// `.
	*                       / \\||| : |||// \
	*                     / _||||| -:- |||||- \
	*                       | | \\\ - /// | |
	*                     | \_| ''\---/'' | |
	*                      \ .-\__ `-` ___/-. /
	*                   ___`. .' /--.--\ `. . __
	*                ."" '< `.___\_<|>_/___.' >'"".
	*               | | : `- \`.;`\ _ /`;.`/ - ` : | |
	*                 \ \ `-. \_ __\ /__ _/ .-` / /
	*         ======`-.____`-.___\_____/___.-`____.-'======
	*                            `=---='
	*
	*         .............................................
	*                  佛祖保佑             永无BUG
	*         佛曰:
	*                  写字楼里写字间，写字间里程序员；
	*                  程序人员写程序，又拿程序换酒钱。
	*                  酒醒只在网上坐，酒醉还来网下眠；
	*                  酒醉酒醒日复日，网上网下年复年。
	*                  但愿老死电脑间，不愿鞠躬老板前；
	*                  奔驰宝马贵者趣，公交自行程序员。
	*                  别人笑我忒疯癫，我笑自己命太贱；
	*                  不见满街漂亮妹，哪个归得程序员？
	*/
	
	
	/**
	 *　　　　　　　　┏┓　　　┏┓+ +
	 *　　　　　　　┏┛┻━━━┛┻┓ + +
	 *　　　　　　　┃　　　　　　　┃
	 *　　　　　　　┃　　　━　　　┃ ++ + + +
	 *　　　　　　 ████━████ ┃+
	 *　　　　　　　┃　　　　　　　┃ +
	 *　　　　　　　┃　　　┻　　　┃
	 *　　　　　　　┃　　　　　　　┃ + +
	 *　　　　　　　┗━┓　　　┏━┛
	 *　　　　　　　　　┃　　　┃
	 *　　　　　　　　　┃　　　┃ + + + +
	 *　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
	 *　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
	 *　　　　　　　　　┃　　　┃
	 *　　　　　　　　　┃　　　┃　　+
	 *　　　　　　　　　┃　 　　┗━━━┓ + +
	 *　　　　　　　　　┃ 　　　　　　　┣┓
	 *　　　　　　　　　┃ 　　　　　　　┏┛
	 *　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
	 *　　　　　　　　　　┃┫┫　┃┫┫
	 *　　　　　　　　　　┗┻┛　┗┻┛+ + + +
	 */
	
	
	/**
	 * 　　　　　　　　┏┓　　　┏┓
	 * 　　　　　　　┏┛┻━━━┛┻┓
	 * 　　　　　　　┃　　　　　　　┃
	 * 　　　　　　　┃　　　━　　　┃
	 * 　　　　　　　┃　＞　　　＜　┃
	 * 　　　　　　　┃　　　　　　　┃
	 * 　　　　　　　┃...　⌒　...　┃
	 * 　　　　　　　┃　　　　　　　┃
	 * 　　　　　　　┗━┓　　　┏━┛
	 * 　　　　　　　　　┃　　　┃　Code is far away from bug with the animal protecting
	 * 　　　　　　　　　┃　　　┃   神兽保佑,代码无bug
	 * 　　　　　　　　　┃　　　┃
	 * 　　　　　　　　　┃　　　┃
	 * 　　　　　　　　　┃　　　┃
	 * 　　　　　　　　　┃　　　┃
	 * 　　　　　　　　　┃　　　┗━━━┓
	 * 　　　　　　　　　┃　　　　　　　┣┓
	 * 　　　　　　　　　┃　　　　　　　┏┛
	 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
	 * 　　　　　　　　　　┃┫┫　┃┫┫
	 * 　　　　　　　　　　┗┻┛　┗┻┛
	 */
	
	
	/**                                                                            
	 *          .,:,,,                                        .::,,,::.            
	 *        .::::,,;;,                                  .,;;:,,....:i:           
	 *        :i,.::::,;i:.      ....,,:::::::::,....   .;i:,.  ......;i.          
	 *        :;..:::;::::i;,,:::;:,,,,,,,,,,..,.,,:::iri:. .,:irsr:,.;i.          
	 *        ;;..,::::;;;;ri,,,.                    ..,,:;s1s1ssrr;,.;r,          
	 *        :;. ,::;ii;:,     . ...................     .;iirri;;;,,;i,          
	 *        ,i. .;ri:.   ... ............................  .,,:;:,,,;i:          
	 *        :s,.;r:... ....................................... .::;::s;          
	 *        ,1r::. .............,,,.,,:,,........................,;iir;          
	 *        ,s;...........     ..::.,;:,,.          ...............,;1s          
	 *       :i,..,.              .,:,,::,.          .......... .......;1,         
	 *      ir,....:rrssr;:,       ,,.,::.     .r5S9989398G95hr;. ....,.:s,        
	 *     ;r,..,s9855513XHAG3i   .,,,,,,,.  ,S931,.,,.;s;s&BHHA8s.,..,..:r:       
	 *    :r;..rGGh,  :SAG;;G@BS:.,,,,,,,,,.r83:      hHH1sXMBHHHM3..,,,,.ir.      
	 *   ,si,.1GS,   sBMAAX&MBMB5,,,,,,:,,.:&8       3@HXHBMBHBBH#X,.,,,,,,rr      
	 *   ;1:,,SH:   .A@&&B#&8H#BS,,,,,,,,,.,5XS,     3@MHABM&59M#As..,,,,:,is,     
	 *  .rr,,,;9&1   hBHHBB&8AMGr,,,,,,,,,,,:h&&9s;   r9&BMHBHMB9:  . .,,,,;ri.    
	 *  :1:....:5&XSi;r8BMBHHA9r:,......,,,,:ii19GG88899XHHH&GSr.      ...,:rs.    
	 *  ;s.     .:sS8G8GG889hi.        ....,,:;:,.:irssrriii:,.        ...,,i1,    
	 *  ;1,         ..,....,,isssi;,        .,,.                      ....,.i1,    
	 *  ;h:               i9HHBMBBHAX9:         .                     ...,,,rs,    
	 *  ,1i..            :A#MBBBBMHB##s                             ....,,,;si.    
	 *  .r1,..        ,..;3BMBBBHBB#Bh.     ..                    ....,,,,,i1;     
	 *   :h;..       .,..;,1XBMMMMBXs,.,, .. :: ,.               ....,,,,,,ss.     
	 *    ih: ..    .;;;, ;;:s58A3i,..    ,. ,.:,,.             ...,,,,,:,s1,      
	 *    .s1,....   .,;sh,  ,iSAXs;.    ,.  ,,.i85            ...,,,,,,:i1;       
	 *     .rh: ...     rXG9XBBM#M#MHAX3hss13&&HHXr         .....,,,,,,,ih;        
	 *      .s5: .....    i598X&&A&AAAAAA&XG851r:       ........,,,,:,,sh;         
	 *      . ihr, ...  .         ..                    ........,,,,,;11:.         
	 *         ,s1i. ...  ..,,,..,,,.,,.,,.,..       ........,,.,,.;s5i.           
	 *          .:s1r,......................       ..............;shs,             
	 *          . .:shr:.  ....                 ..............,ishs.               
	 *              .,issr;,... ...........................,is1s;.                 
	 *                 .,is1si;:,....................,:;ir1sr;,                    
	 *                    ..:isssssrrii;::::::;;iirsssssr;:..                      
	 *                         .,::iiirsssssssssrri;;:.                        
	 */   
	
	
	/**  
	 *               ii.                                         ;9ABH,            
	 *              SA391,                                    .r9GG35&G            
	 *              &#ii13Gh;                               i3X31i;:,rB1           
	 *              iMs,:,i5895,                         .5G91:,:;:s1:8A           
	 *               33::::,,;5G5,                     ,58Si,,:::,sHX;iH1          
	 *                Sr.,:;rs13BBX35hh11511h5Shhh5S3GAXS:.,,::,,1AG3i,GG          
	 *                .G51S511sr;;iiiishS8G89Shsrrsh59S;.,,,,,..5A85Si,h8          
	 *               :SB9s:,............................,,,.,,,SASh53h,1G.         
	 *            .r18S;..,,,,,,,,,,,,,,,,,,,,,,,,,,,,,....,,.1H315199,rX,         
	 *          ;S89s,..,,,,,,,,,,,,,,,,,,,,,,,....,,.......,,,;r1ShS8,;Xi         
	 *        i55s:.........,,,,,,,,,,,,,,,,.,,,......,.....,,....r9&5.:X1         
	 *       59;.....,.     .,,,,,,,,,,,...        .............,..:1;.:&s         
	 *      s8,..;53S5S3s.   .,,,,,,,.,..      i15S5h1:.........,,,..,,:99         
	 *      93.:39s:rSGB@A;  ..,,,,.....    .SG3hhh9G&BGi..,,,,,,,,,,,,.,83        
	 *      G5.G8  9#@@@@@X. .,,,,,,.....  iA9,.S&B###@@Mr...,,,,,,,,..,.;Xh       
	 *      Gs.X8 S@@@@@@@B:..,,,,,,,,,,. rA1 ,A@@@@@@@@@H:........,,,,,,.iX:      
	 *     ;9. ,8A#@@@@@@#5,.,,,,,,,,,... 9A. 8@@@@@@@@@@M;    ....,,,,,,,,S8      
	 *     X3    iS8XAHH8s.,,,,,,,,,,...,..58hH@@@@@@@@@Hs       ...,,,,,,,:Gs     
	 *    r8,        ,,,...,,,,,,,,,,.....  ,h8XABMMHX3r.          .,,,,,,,.rX:    
	 *   :9, .    .:,..,:;;;::,.,,,,,..          .,,.               ..,,,,,,.59    
	 *  .Si      ,:.i8HBMMMMMB&5,....                    .            .,,,,,.sMr   
	 *  SS       :: h@@@@@@@@@@#; .                     ...  .         ..,,,,iM5   
	 *  91  .    ;:.,1&@@@@@@MXs.                            .          .,,:,:&S   
	 *  hS ....  .:;,,,i3MMS1;..,..... .  .     ...                     ..,:,.99   
	 *  ,8; ..... .,:,..,8Ms:;,,,...                                     .,::.83   
	 *   s&: ....  .sS553B@@HX3s;,.    .,;13h.                            .:::&1   
	 *    SXr  .  ...;s3G99XA&X88Shss11155hi.                             ,;:h&,   
	 *     iH8:  . ..   ,;iiii;,::,,,,,.                                 .;irHA    
	 *      ,8X5;   .     .......                                       ,;iihS8Gi  
	 *         1831,                                                 .,;irrrrrs&@  
	 *           ;5A8r.                                            .:;iiiiirrss1H  
	 *             :X@H3s.......                                .,:;iii;iiiiirsrh  
	 *              r#h:;,...,,.. .,,:;;;;;:::,...              .:;;;;;;iiiirrss1  
	 *             ,M8 ..,....,.....,,::::::,,...         .     .,;;;iiiiiirss11h  
	 *             8B;.,,,,,,,.,.....          .           ..   .:;;;;iirrsss111h  
	 *            i@5,:::,,,,,,,,.... .                   . .:::;;;;;irrrss111111  
	 *            9Bi,:,,,,......                        ..r91;;;;;iirrsss1ss1111  
	 */  
	
	
	/**  
	 * _ooOoo_  
	 * o8888888o  
	 * 88" . "88  
	 * (| -_- |)  
	 *  O\ = /O  
	 * ___/`---'\____  
	 * .   ' \\| |// `.  
	 * / \\||| : |||// \  
	 * / _||||| -:- |||||- \  
	 * | | \\\ - /// | |  
	 * | \_| ''\---/'' | |  
	 * \ .-\__ `-` ___/-. /  
	 * ___`. .' /--.--\ `. . __  
	 * ."" '< `.___\_<|>_/___.' >'"".  
	 * | | : `- \`.;`\ _ /`;.`/ - ` : | |  
	 * \ \ `-. \_ __\ /__ _/ .-` / /  
	 * ======`-.____`-.___\_____/___.-`____.-'======  
	 * `=---='  
	 *          .............................................  
	 *           佛曰：bug泛滥，我已瘫痪！  
	 */  
	
	
	/**  
	 *                    .::::.  
	 *                  .::::::::.  
	 *                 :::::::::::  FUCK YOU  
	 *             ..:::::::::::'  
	 *           '::::::::::::'  
	 *             .::::::::::  
	 *        '::::::::::::::..  
	 *             ..::::::::::::.  
	 *           ``::::::::::::::::  
	 *            ::::``:::::::::'        .:::.  
	 *           ::::'   ':::::'       .::::::::.  
	 *         .::::'      ::::     .:::::::'::::.  
	 *        .:::'       :::::  .:::::::::' ':::::.  
	 *       .::'        :::::.:::::::::'      ':::::.  
	 *      .::'         ::::::::::::::'         ``::::.  
	 *  ...:::           ::::::::::::'              ``::.  
	 * ```` ':.          ':::::::::'                  ::::..  
	 *                    '.:::::'                    ':'````..  
	 */ 
	
	
	/**                              _  
	 *  _._ _..._ .-',     _.._(`))  
	 * '-. `     '  /-._.-'    ',/  
	 *    )         \            '.  
	 *   / _    _    |             \  
	 *  |  a    a    /              |  
	 *  \   .-.                     ;  
	 *   '-('' ).-'       ,'       ;  
	 *      '-;           |      .'  
	 *         \           \    /  
	 *         | 7  .__  _.-\   \  
	 *         | |  |  ``/  /`  /  
	 *        /,_|  |   /,_/   /  
	 *           /,_/      '`-'  
	 */  
	
	
	/**  
	 **************************************************************  
	 *                                                            *  
	 *   .=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-.       *  
	 *    |                     ______                     |      *  
	 *    |                  .-"      "-.                  |      *  
	 *    |                 /            \                 |      *  
	 *    |     _          |              |          _     |      *  
	 *    |    ( \         |,  .-.  .-.  ,|         / )    |      *  
	 *    |     > "=._     | )(__/  \__)( |     _.=" <     |      *  
	 *    |    (_/"=._"=._ |/     /\     \| _.="_.="\_)    |      *  
	 *    |           "=._"(_     ^^     _)"_.="           |      *  
	 *    |               "=\__|IIIIII|__/="               |      *  
	 *    |              _.="| \IIIIII/ |"=._              |      *  
	 *    |    _     _.="_.="\          /"=._"=._     _    |      *  
	 *    |   ( \_.="_.="     `--------`     "=._"=._/ )   |      *  
	 *    |    > _.="                            "=._ <    |      *  
	 *    |   (_/                                    \_)   |      *  
	 *    |                                                |      *  
	 *    '-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-='      *  
	 *                                                            *  
	 *           LASCIATE OGNI SPERANZA, VOI CH'ENTRATE           *  
	 **************************************************************  
	 */  
	
	
	/**  
	 *                                         ,s555SB@@&                            
	 *                                      :9H####@@@@@Xi                          
	 *                                     1@@@@@@@@@@@@@@8                         
	 *                                   ,8@@@@@@@@@B@@@@@@8                        
	 *                                  :B@@@@X3hi8Bs;B@@@@@Ah,                     
	 *             ,8i                  r@@@B:     1S ,M@@@@@@#8;                   
	 *            1AB35.i:               X@@8 .   SGhr ,A@@@@@@@@S                  
	 *            1@h31MX8                18Hhh3i .i3r ,A@@@@@@@@@5                 
	 *            ;@&i,58r5                 rGSS:     :B@@@@@@@@@@A                 
	 *             1#i  . 9i                 hX.  .: .5@@@@@@@@@@@1                 
	 *              sG1,  ,G53s.              9#Xi;hS5 3B@@@@@@@B1                  
	 *               .h8h.,A@@@MXSs,           #@H1:    3ssSSX@1                    
	 *               s ,@@@@@@@@@@@@Xhi,       r#@@X1s9M8    .GA981                 
	 *               ,. rS8H#@@@@@@@@@@#HG51;.  .h31i;9@r    .8@@@@BS;i;            
	 *                .19AXXXAB@@@@@@@@@@@@@@#MHXG893hrX#XGGXM@@@@@@@@@@MS          
	 *                s@@MM@@@hsX#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&,        
	 *              :GB@#3G@@Brs ,1GM@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@B,       
	 *            .hM@@@#@@#MX 51  r;iSGAM@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@8       
	 *          :3B@@@@@@@@@@@&9@h :Gs   .;sSXH@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@:      
	 *      s&HA#@@@@@@@@@@@@@@M89A;.8S.       ,r3@@@@@@@@@@@@@@@@@@@@@@@@@@@r      
	 *   ,13B@@@@@@@@@@@@@@@@@@@5 5B3 ;.         ;@@@@@@@@@@@@@@@@@@@@@@@@@@@i      
	 *  5#@@#&@@@@@@@@@@@@@@@@@@9  .39:          ;@@@@@@@@@@@@@@@@@@@@@@@@@@@;      
	 *  9@@@X:MM@@@@@@@@@@@@@@@#;    ;31.         H@@@@@@@@@@@@@@@@@@@@@@@@@@:      
	 *   SH#@B9.rM@@@@@@@@@@@@@B       :.         3@@@@@@@@@@@@@@@@@@@@@@@@@@5      
	 *     ,:.   9@@@@@@@@@@@#HB5                 .M@@@@@@@@@@@@@@@@@@@@@@@@@B      
	 *           ,ssirhSM@&1;i19911i,.             s@@@@@@@@@@@@@@@@@@@@@@@@@@S     
	 *              ,,,rHAri1h1rh&@#353Sh:          8@@@@@@@@@@@@@@@@@@@@@@@@@#:    
	 *            .A3hH@#5S553&@@#h   i:i9S          #@@@@@@@@@@@@@@@@@@@@@@@@@A.   
	 *  
	 *  
	 *    又看源码，看你妹妹呀！  
	 */ 
	
	/**  
	 *_______________#########_______________________   
	 *______________############_____________________   
	 *______________#############____________________   
	 *_____________##__###########___________________   
	 *____________###__######_#####__________________   
	 *____________###_#######___####_________________   
	 *___________###__##########_####________________   
	 *__________####__###########_####_______________   
	 *________#####___###########__#####_____________   
	 *_______######___###_########___#####___________   
	 *_______#####___###___########___######_________   
	 *______######___###__###########___######_______   
	 *_____######___####_##############__######______   
	 *____#######__#####################_#######_____   
	 *____#######__##############################____   
	 *___#######__######_#################_#######___   
	 *___#######__######_######_#########___######___   
	 *___#######____##__######___######_____######___   
	 *___#######________######____#####_____#####____   
	 *____######________#####_____#####_____####_____   
	 *_____#####________####______#####_____###______   
	 *______#####______;###________###______#________   
	 *________##_______####________####______________   
	 */  
	
	
	/**  
	 * http://www.freebuf.com/  
	 *           _.._        ,------------.  
	 *        ,'      `.    ( We want you! )  
	 *       /  __) __` \    `-,----------'  
	 *      (  (`-`(-')  ) _.-'  
	 *      /)  \  = /  (  
	 *     /'    |--' .  \  
	 *    (  ,---|  `-.)__`  
	 *     )(  `-.,--'   _`-.  
	 *    '/,'          (  Uu",  
	 *     (_       ,    `/,-' )  
	 *     `.__,  : `-'/  /`--'  
	 *       |     `--'  |  
	 *       `   `-._   /  
	 *        \        (  
	 *        /\ .      \.  freebuf  
	 *       / |` \     ,-\  
	 *      /  \| .)   /   \  
	 *     ( ,'|\    ,'     :  
	 *     | \,`.`--"/      }  
	 *     `,'    \  |,'    /  
	 *    / "-._   `-/      |  
	 *    "-.   "-.,'|     ;  
	 *   /        _/["---'""]  
	 *  :        /  |"-     '  
	 *  '           |      /  
	 *              `      |  
	 */  
	  
	  
	/**   
	 * https://campus.alibaba.com/  
	 *                                 `:::::::::::,  
	 *                             `::;:::::::;:::::::,  `  
	 *                          `::;;:::::::@@@@;:::::::`  
	 *                        ,:::::::::::::@    #@':::::`  
	 *                      :::::::::::::::'@@      @;::::  
	 *                    ::::::::::::'@@@@'```      .+:::`  
	 *                  ::::::::::;@@@#.              ,:::,  
	 *                .::::::::+@#@`                   ::::  
	 *               :::::::+@@'                       ::::  
	 *             `:::::'@@:                         `:::.  
	 *            ,::::@@:  `                         ::::  
	 *           ;::::::@                            .:::;  
	 *          :;:::::;@`        `                  :::;  
	 *         :::::::::@`        @                 ;::::  
	 *        :::::::::#`          @`              ,::::  
	 *       :::::::::@`         +@ @             .::::`  
	 *      .::::::'@@`       `@@'  @             ::::,  
	 *      :::::::++@@@@@@@@@@.                 ::::;  
	 *     ;:::::::+,   `..`                    :::::  
	 *    ,::::::::',                          :::::  
	 *    :::::::::+,                         :::::`  
	 *   :::::::::+@.                        ,::::.`                     `,  
	 *   ::::::;;@+                         .::;::                     `;  
	 *  :::::::@@                          `:::;:                   `::``  
	 *  ::::::#@                           ;::::                  .::`  
	 *  :::::;@                           :::::`               .;::`  
	 *  :::::@                           `:;:::            `::::;  
	 *  :::::#                           :::::.        `,;:::::  
	 *  ::::::                    `      ::::::,.,::::::::::.  
	 *  ,::::::`              .::        ::::::::::::::::;`  
	 *   ;::::::::,````.,:::::,          ::::::::::::::.  
	 *    :::::::::::::::::: `           `::::::::::`  
	 *     `::::::::::::,                  .:::.  
	 *         `..`  
	 */  
	  
	  
	/**  
	 * http://www.flvcd.com/  
	 *  .--,       .--,  
	 * ( (  \.---./  ) )  
	 *  '.__/o   o\__.'  
	 *     {=  ^  =}  
	 *      >  -  <  
	 *     /       \  
	 *    //       \\  
	 *   //|   .   |\\  
	 *   "'\       /'"_.-~^`'-.  
	 *      \  _  /--'         `  
	 *    ___)( )(___  
	 *   (((__) (__)))    高山仰止,景行行止.虽不能至,心向往之。  
	 */  
	  
	  
	/**  
	 * 頂頂頂頂頂頂頂頂頂　頂頂頂頂頂頂頂頂頂  
	 * 頂頂頂頂頂頂頂　　　　　頂頂　　　　　  
	 * 　　　頂頂　　　頂頂頂頂頂頂頂頂頂頂頂  
	 * 　　　頂頂　　　頂頂頂頂頂頂頂頂頂頂頂  
	 * 　　　頂頂　　　頂頂　　　　　　　頂頂  
	 * 　　　頂頂　　　頂頂　　頂頂頂　　頂頂  
	 * 　　　頂頂　　　頂頂　　頂頂頂　　頂頂  
	 * 　　　頂頂　　　頂頂　　頂頂頂　　頂頂  
	 * 　　　頂頂　　　頂頂　　頂頂頂　　頂頂  
	 * 　　　頂頂　　　　　　　頂頂頂　  
	 * 　　　頂頂　　　　　　頂頂　頂頂　頂頂  
	 * 　頂頂頂頂　　　頂頂頂頂頂　頂頂頂頂頂  
	 * 　頂頂頂頂　　　頂頂頂頂　　　頂頂頂頂  
	 */  

	
	
	/**  
	 * You may think you know what the following code does.  
	 * But you dont. Trust me.  
	 * Fiddle with it, and youll spend many a sleepless  
	 * night cursing the moment you thought youd be clever  
	 * enough to "optimize" the code below.  
	 * Now close this file and go play with something else.  
	 */  
	/**  
	 * 你可能会认为你读得懂以下的代码。但是你不会懂的，相信我吧。  
	 * 要是你尝试玩弄这段代码的话，你将会在无尽的通宵中不断地咒骂自己为什么会认为自己聪明到可以优化这段代码。  
	 * 现在请关闭这个文件去玩点别的吧。  
	 */  
	
	/**  
	 * For the brave souls who get this far: You are the chosen ones,  
	 * the valiant knights of programming who toil away, without rest,  
	 * fixing our most awful code. To you, true saviors, kings of men,  
	 * I say this: never gonna give you up, never gonna let you down,  
	 * never gonna run around and desert you. Never gonna make you cry,  
	 * never gonna say goodbye. Never gonna tell a lie and hurt you.  
	 */  
	/**  
	 * 致终于来到这里的勇敢的人：   
	 * 你是被上帝选中的人，是英勇的、不敌辛苦的、不眠不休的来修改我们这最棘手的代码的编程骑士。  
	 * 你，我们的救世主，人中之龙，我要对你说：永远不要放弃，永远不要对自己失望，永远不要逃走，辜负了自己，  
	 * 永远不要哭啼，永远不要说再见，永远不要说谎来伤害自己。  
	 */  
	
	/**
		1.伟大的团队靠的是信任，不同意但服从讨论的时候可以海阔天空，但形成了决议就一定要坚决的执行.
		2.衡量代码耦合是否严重,完全可以统计对代码的copy程度.
		3.创业: 开展了一个业务，找到了盈利的模式，并且可以持续发展下去
		想实现这种成功，最好就是熟知某个领域/行业，真的发现了这个领域/行业的痛点问题，自己也有了足够的人脉和资源，这时候出来创业成功的可能性最大。 如果再加上足够好的运气，未来不可限量。
		一定要以最小的成本验证你的想法/你的商业模式， 看看是不是“伪需求” ，减少沉没成本，及时上岸

	  	分布式（distributed）是指在多台不同的服务器中部署不同的服务模块，通过远程调用协同工作，对外提供服务。
		集群（cluster）是指在多台不同的服务器中部署相同应用或服务模块，构成一个集群，通过负载均衡设备对外提供服务。
	 */
	
	/**
	 2018-08-14
	 	小结：
	 		1.开发要有一个熟悉和方便的环境,多读源码,多debug,
	 			eclipse + jdk8 + redis + maven + tomcat + git + mysql  
	 		2.学习技术和线上测试环境
	 		 	linux + zookeeper + searchEngline（elasticsearch，solr） + mysql + docker 
	 			+mq  + nexus + jenkins + gitlab + docker + nginx 
	 		3.基本功
	 			spring + mybatis + springcloud + dubbo 
	 		4.工具
	 			chrome + plugin [postman + fiddle + insightio ]
	 			Tampermonkey
	 			https://greasyfork.org/zh-CN/scripts 
	 			1.文本编译器  	sublimetext 
	 			2.pdf阅读器 	foxitReader
	 			3.有道词典		
	 			4.everything
	 			5.vmware(伪)集群   + xshell 
	 		5.因为有些点好长时间都没有用到,难免有些生疏,因而目前项目里面还有比较多的链接,
	 		后续慢慢会一点点啃掉.
	 		6.ReferenceLibrary 和 Maven Dependency , Java System Library.
	 		
	 			
	 */
	

}
