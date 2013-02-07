package com.runescape;

import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.zip.CRC32;

import com.runescape.anim.Animation;
import com.runescape.anim.AnimationSequence;
import com.runescape.anim.SpotAnimation;
import com.runescape.cache.Archive;
import com.runescape.cache.Index;
import com.runescape.cache.def.ActorDefinition;
import com.runescape.cache.def.FloorDefinition;
import com.runescape.cache.def.GameObjectDefinition;
import com.runescape.cache.def.IdentityKit;
import com.runescape.cache.def.ItemDefinition;
import com.runescape.cache.def.VarBit;
import com.runescape.cache.def.Varp;
import com.runescape.cache.requester.OnDemandNode;
import com.runescape.cache.requester.OnDemandRequester;
import com.runescape.graphic.ImageRGB;
import com.runescape.graphic.IndexedImage;
import com.runescape.graphic.Model;
import com.runescape.graphic.ProducingGraphicsBuffer;
import com.runescape.graphic.Rasterizer;
import com.runescape.graphic.Rasterizer3D;
import com.runescape.graphic.TypeFace;
import com.runescape.graphic.Widget;
import com.runescape.net.Buffer;
import com.runescape.net.BufferedConnection;
import com.runescape.net.ISAACCipher;
import com.runescape.node.LinkedList;
import com.runescape.renderable.Actor;
import com.runescape.renderable.Item;
import com.runescape.renderable.Npc;
import com.runescape.renderable.Player;
import com.runescape.renderable.Projectile;
import com.runescape.scene.AnimableObject;
import com.runescape.scene.CollisionMap;
import com.runescape.scene.FloorDecoration;
import com.runescape.scene.GameObject;
import com.runescape.scene.Region;
import com.runescape.scene.Scene;
import com.runescape.scene.SceneSpawnRequest;
import com.runescape.scene.SpawnObjectNode;
import com.runescape.scene.Wall;
import com.runescape.scene.WallDecoration;
import com.runescape.sound.SoundTrack;
import com.runescape.util.ChatCensor;
import com.runescape.util.ChatEncoder;
import com.runescape.util.MouseCapturer;
import com.runescape.util.PacketConstants;
import com.runescape.util.SignLink;
import com.runescape.util.SkillConstants;
import com.runescape.util.TextUtils;

@SuppressWarnings("serial")
public class Client extends GameShell {

	public static final boolean JAGGRAB = false;
	public static final boolean RSA = false;
	private int ignoreListCount;
	private long aLong849;
	private int[][] distanceValues = new int[104][104];
	private int[] friendsListWorlds = new int[200];
	private LinkedList[][][] groundItemNodes = new LinkedList[4][104][104];
	private int[] anIntArray853;
	private int[] anIntArray854;
	private volatile boolean aBoolean856 = false;
	private Socket jaggrabSocket;
	private int loginScreenState;
	private Buffer aBuffer859 = new Buffer(new byte[5000]);
	private Npc[] actors = new Npc[16384];
	private int actorCount;
	protected int[] anIntArray862 = new int[16384];
	private int anInt864;
	protected int[] anIntArray865 = new int[1000];
	private int anInt866;
	private int anInt867;
	private int anInt868;
	private String aString869;
	private int privateChatSetting;
	private static int anInt871;
	private Buffer aBuffer872 = Buffer.newPooledBuffer(1);
	private boolean aBoolean873 = true;
	private int[] anIntArray875;
	private int[] anIntArray876;
	private int[] anIntArray877;
	private int[] anIntArray878;
	private static int anInt879;
	private int hintIconType;
	private static BigInteger RSA_MODULUS = new BigInteger(
			"7162900525229798032761816791230527296329313291232324290237849263501208207972894053929065636522363163621000728841182238772712427862772219676577293600221789");
	private int openWidgetIndex = -1;
	private int anInt883;
	private int anInt884;
	private int anInt885;
	private int anInt886;
	private int anInt887;
	private int playerRights;
	private int[] skillExperience = new int[SkillConstants.SKILL_COUNT];
	private IndexedImage anIndexedImage890;
	private IndexedImage anIndexedImage891;
	private IndexedImage anIndexedImage892;
	private IndexedImage anIndexedImage893;
	private IndexedImage anIndexedImage894;
	private ImageRGB mapFlagMarker;
	private ImageRGB anImageRGB896;
	private boolean aBoolean897 = false;
	private int[] anIntArray898 = new int[5];
	private int anInt899 = -1;
	private int anInt900 = -680;
	private boolean[] aBooleanArray901 = new boolean[5];
	private int playerWeight;
	protected MouseCapturer mouseCapturer;
	private volatile boolean aBoolean905 = false;
	private String reportWidgetName = "";
	private int anInt909 = -1;
	private boolean actionMenuOpen = false;
	private int anInt911;
	private String chatboxInput = "";
	private final int MAX_PLAYERS = 2048;
	private int localPlayerIndex = 2047;
	private Player[] players = new Player[MAX_PLAYERS];
	private int playerCount;
	protected int[] anIntArray917 = new int[MAX_PLAYERS];
	private int anInt918;
	private int[] anIntArray919 = new int[MAX_PLAYERS];
	private Buffer[] playerBuffer = new Buffer[MAX_PLAYERS];
	private int anInt921;
	private int anInt922 = 1;
	private int friendsListCount;
	private int friendListStatus;
	private int[][] wayPoints = new int[104][104];
	private int anInt927 = 7759444;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer928;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer929;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer930;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer931;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer932;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer933;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer934;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer935;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer936;
	private byte[] aByteArray937 = new byte[16384];
	private int anInt938;
	private int anInt939;
	private int anInt940;
	private int anInt941;
	private int anInt942;
	private int currentSceneIndex;
	private int[] skillLevel = new int[SkillConstants.SKILL_COUNT];
	private static int anInt949;
	private long[] ignoreList = new long[100];
	private boolean loadingError = false;
	private int anInt952 = 3353893;
	private int[] anIntArray953 = new int[5];
	private int[][] anIntArrayArray954 = new int[104][104];
	private CRC32 crc32 = new CRC32();
	private ImageRGB anImageRGB956;
	private ImageRGB anImageRGB957;
	private int hintIconPlayerIndex;
	private int hintIconX;
	private int hintIconY;
	private int hintIconOffset;
	private int anInt962;
	private int anInt963;
	private int[] chatboxMessageType = new int[100];
	private String[] chatboxMessageName = new String[100];
	private String[] chatboxMessage = new String[100];
	private int anInt970;
	private Scene currentScene;
	private IndexedImage[] anIndexedImageArray972 = new IndexedImage[13];
	private int anInt973;
	private int anInt974;
	private int anInt975;
	private int anInt976;
	private int anInt977;
	private long aLong978;
	protected boolean aBoolean979 = true;
	private long[] friendsListLongs = new long[200];
	private int songIndex = -1;
	private static int nodeIndex = 10;
	public static int portOffset;
	private static boolean membersWorld = true;
	private static boolean lowMemory;
	private volatile boolean aBoolean987 = false;
	private int anInt988 = -1;
	private int anInt989 = -1;
	private int[] anIntArray990 = { 0xFFFF00, 16711680, 65280, 65535, 16711935, 0xFFFFFF };
	private IndexedImage titleboxImage;
	private IndexedImage titleboxButtonImage;
	private int[] anIntArray993 = new int[33];
	private int[] anIntArray994 = new int[256];
	public Index[] stores = new Index[5];
	public int[] settings = new int[2000];
	private boolean aBoolean997 = false;
	private int anInt999;
	private int anInt1000 = 50;
	private int[] anIntArray1001 = new int[anInt1000];
	private int[] anIntArray1002 = new int[anInt1000];
	private int[] anIntArray1003 = new int[anInt1000];
	private int[] anIntArray1004 = new int[anInt1000];
	private int[] anIntArray1005 = new int[anInt1000];
	private int[] anIntArray1006 = new int[anInt1000];
	private int[] anIntArray1007 = new int[anInt1000];
	private String[] aStringArray1008 = new String[anInt1000];
	private int anInt1009;
	private int lastSceneIndex = -1;
	private static int anInt1011;
	private ImageRGB[] anImageRGBArray1012 = new ImageRGB[20];
	private int anInt1013;
	private int anInt1014;
	private int[] anIntArray1015 = new int[5];
	private int anInt1017;
	private int anInt1020;
	private int anInt1021;
	private int anInt1022;
	private int anInt1023;
	private int anInt1024;
	private ISAACCipher isaacCipher;
	private ImageRGB anImageRGB1026;
	private int anInt1027 = 2301979;
	public static final int[][] anIntArrayArray1028 = {
			{ 6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983, 54193 },
			{ 8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003, 25239 },
			{ 25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003 },
			{ 4626, 11146, 6439, 12, 4758, 10270 }, { 4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574 } };
	private String inputInputMessage = "";
	private static int anInt1030;
	private int lastLogin;
	private int packetSize;
	private int opcode;
	private int anInt1034;
	private int anInt1035;
	private int anInt1036;
	private byte aByte1037 = 24;
	private LinkedList projectileList = new LinkedList();
	private int anInt1039;
	private int anInt1040;
	private int anInt1041;
	private boolean aBoolean1042 = false;
	private int walkablewidgetIndex = -1;
	private static int[] xpForSkillLevel = new int[99];
	private int minimapState;
	protected int anInt1047;
	private int anInt1048;
	private IndexedImage scrollbarUp;
	private IndexedImage scrollbarDown;
	private int anInt1051;
	private IndexedImage anIndexedImage1052;
	private IndexedImage anIndexedImage1053;
	private IndexedImage anIndexedImage1054;
	private int[] anIntArray1055 = new int[5];
	private boolean aBoolean1056 = false;
	private static BigInteger RSA_EXPONENT;
	private ImageRGB[] worldMapHintIcons = new ImageRGB[100];
	private int regionAbsoluteBaseX;
	private int regionAbsoluteBaseY;
	private int anInt1061;
	private int anInt1062;
	private int anInt1063;
	private int anInt1064;
	private int anInt1065;
	private int anInt1066;
	private int anInt1067 = -1;
	private int[] skillMaxLevel = new int[SkillConstants.SKILL_COUNT];
	private int[] defaultSettings = new int[2000];
	private int anInt1071;
	private boolean aBoolean1072 = true;
	private int anInt1073;
	private String aString1074;
	private static int anInt1076;
	private int[] anIntArray1077 = new int[151];
	private Archive anArchive1078;
	private int flashingSidebar = -1;
	private int anInt1080;
	private LinkedList aLinkedList1081 = new LinkedList();
	private int[] anIntArray1082 = new int[33];
	private Widget aWidget1084 = new Widget();
	private IndexedImage[] anIndexedImageArray1085 = new IndexedImage[100];
	static int anInt1086;
	private int trackCount;
	private int anInt1088 = 5063219;
	private int anInt1089;
	private int[] anIntArray1090 = new int[7];
	private int anInt1091;
	private int anInt1092;
	private OnDemandRequester onDemandRequester;
	private int anInt1094;
	private int anInt1095;
	private int minimapHintCount;
	private int[] minimapHintX = new int[1000];
	private int[] minimapHintY = new int[1000];
	private ImageRGB mapdotItem;
	private ImageRGB mapdotActor;
	private ImageRGB mapdotPlayer;
	private ImageRGB mapdotFriend;
	private ImageRGB mapdotTeammate;
	private int anInt1104;
	private boolean aBoolean1105 = false;
	private int anInt1106 = -733;
	private String[] friendsListNames = new String[200];
	private Buffer inBuffer = Buffer.newPooledBuffer(1);
	private int anInt1109;
	private int anInt1110;
	private int anInt1111;
	private int anInt1112;
	private int anInt1113;
	private int anInt1114;
	private int[] crcValues = new int[9];
	private int[] menuActionIndex2 = new int[500];
	private int[] menuActionIndex3 = new int[500];
	private int[] menuActionIndexes = new int[500];
	private int[] menuActionIndex1 = new int[500];
	private ImageRGB[] anImageRGBArray1120 = new ImageRGB[20];
	private static int anInt1122;
	private int anInt1123;
	private int anInt1124;
	private int anInt1125;
	private int anInt1126;
	private int anInt1127;
	private boolean aBoolean1128 = false;
	private int systemUpdateTime;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer1132;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer1133;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer1134;
	private ProducingGraphicsBuffer flameLeftBackground;
	private ProducingGraphicsBuffer flameRightBackground;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer1137;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer1138;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer1139;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer1140;
	private static int anInt1142;
	private int anInt1144 = -77;
	private int membershipAdviser;
	private String inputCustomMessage = "";
	private ImageRGB minimapCompass;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer1148;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer1149;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer1150;
	public static Player localPlayer;
	private String[] playerActions = new String[5];
	private boolean[] aBooleanArray1153 = new boolean[5];
	private int[][][] constructMapTiles = new int[4][13][13];
	private int[] tabWidgetIndexes = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
	private int anInt1156;
	private int anInt1157 = 2;
	private int menuActionRow;
	private static int anInt1159;
	private int anInt1161;
	private int spellId;
	private int anInt1163;
	protected String aString1164;
	private ImageRGB[] minimapHint = new ImageRGB[1000];
	private boolean aBoolean1166 = false;
	private static int anInt1167;
	private IndexedImage anIndexedImage1168;
	private IndexedImage anIndexedImage1169;
	private IndexedImage anIndexedImage1170;
	private IndexedImage anIndexedImage1171;
	private IndexedImage anIndexedImage1172;
	private int playerEnergy;
	private boolean aBoolean1174 = false;
	private ImageRGB[] anImageRGBArray1175 = new ImageRGB[8];
	private boolean musicEnabled = true;
	private IndexedImage[] titleFlameEmblem;
	private boolean drawTabArea = false;
	private int unreadMessages;
	private static int anInt1180;
	static boolean debug;
	public boolean loggedIn = false;
	private boolean aBoolean1183 = false;
	private boolean aBoolean1184 = false;
	private boolean aBoolean1185 = false;
	public static int currentCycle;
	private static String VALID_CHARACTERS;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer1188;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer1189;
	private ProducingGraphicsBuffer currentSceneBuffer;
	private ProducingGraphicsBuffer aProducingGraphicsBuffer1191;
	private int lastRecoveryChange;
	private BufferedConnection bufferedConnection;
	private int anInt1194;
	private int anInt1195;
	private int anInt1196 = 1;
	private long aLong1197;
	private String aString1198 = "";
	private String aString1199 = "";
	private static int anInt1200;
	private boolean genericLoadingError = false;
	private final int[] anIntArray1202 = { 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 };
	private int anInt1203 = -1;
	private LinkedList aLinkedList1204 = new LinkedList();
	private int[] anIntArray1205;
	private int[] anIntArray1206;
	private int[] anIntArray1207;
	private byte[][] aByteArrayArray1208;
	private int anInt1209 = 128;
	private int anInt1210;
	private int anInt1211;
	private int anInt1212;
	private static int anInt1213;
	private int anInt1214 = -1;
	private int[] anIntArray1215;
	private int[] anIntArray1216;
	private Buffer outBuffer = Buffer.newPooledBuffer(1);
	private int lastAddress;
	private int anInt1220;
	private IndexedImage inventoryBackgroundImage;
	private IndexedImage minimapBackgroundImage;
	private IndexedImage chatboxBackgroundImage;
	private String[] menuActionNames = new String[500];
	private ImageRGB anImageRGB1226;
	private ImageRGB anImageRGB1227;
	private int[] anIntArray1228 = new int[5];
	public static final int[] anIntArray1229;
	public static boolean flagged;
	private int[] trackIndexes = new int[50];
	private int anInt1233;
	private int anInt1234;
	private int anInt1235 = 2;
	private int anInt1236 = 78;
	private String chatMessage = "";
	private int anInt1238;
	private int[][][] anIntArrayArrayArray1239;
	private long aLong1240;
	private int anInt1241;
	private IndexedImage[] anIndexedImageArray1244 = new IndexedImage[2];
	protected long aLong1245;
	private int currentTabIndex = 3;
	private int hintIconActorIndex;
	private boolean aBoolean1248 = false;
	private int inputType;
	private static int anInt1251;
	private int onDemandRequesterId;
	private boolean midiFade = true;
	private int[] anIntArray1254 = new int[151];
	private CollisionMap[] currentCollisionMap = new CollisionMap[4];
	private static boolean aBoolean1256;
	public static int[] BITFIELD_MAX_VALUE;
	private boolean aBoolean1258 = false;
	private int[] anIntArray1259;
	private int[] anIntArray1260;
	private int[] anIntArray1261;
	protected int anInt1262;
	protected int anInt1263;
	private int[] anIntArray1265 = new int[100];
	private int[] trackLoop = new int[50];
	private boolean aBoolean1267 = false;
	private int anInt1268;
	private int anInt1269;
	private int anInt1270;
	private int anInt1271;
	private byte[][] aByteArrayArray1272;
	private int tradeSetting;
	private int anInt1274;
	private int[] trackDelay = new int[50];
	private int anInt1276;
	private boolean alreadyLoaded = false;
	private int anInt1278;
	private int anInt1279;
	private boolean drawGameframe = false;
	private boolean aBoolean1281 = false;
	private int anInt1282;
	private byte[][][] currentSceneTileFlags;
	private int songFadeCycle;
	private int destinationX;
	private int destinationY;
	private ImageRGB minimapImage;
	private int arbitraryDestination;
	private int anInt1290;
	private String loginMessage1 = "";
	private String loginMessage2 = "";
	private int playerPositionX;
	private int playerPositionY;
	private TypeFace fontSmall;
	private TypeFace fontNormal;
	private TypeFace fontBold;
	private TypeFace fontFancy;
	private byte aByte1299 = -13;
	private int anInt1300;
	private int chatboxWidgetIndex = -1;
	private int anInt1303;
	private int anInt1304 = 2;
	private int[] walkingQueueX = new int[4000];
	private int[] walkingQueueY = new int[4000];
	private int anInt1307;
	private int anInt1308;
	private int anInt1309;
	private int anInt1310;
	protected String aString1311;
	private int publicChatSetting;
	private static int currentWalkingQueueSize;
	private int anInt1314 = -1;

	private static final String formatNumber(int value) {
		try {
			String s = String.valueOf(value);
			for (int i = s.length() - 3; i > 0; i -= 3) {
				s = s.substring(0, i) + "," + s.substring(i);
			}
			if (s.length() > 8) {
				s = "@gre@" + s.substring(0, s.length() - 8) + " million @whi@(" + s + ")";
			} else if (s.length() > 4) {
				s = "@cya@" + s.substring(0, s.length() - 4) + "K @whi@(" + s + ")";
			}
			return " " + s;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("24548, " + value + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void stopMusic() {
		do {
			try {
				SignLink.midiFade = 0;
				SignLink.nextSongName = "stop";
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("83254, " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void verifyArchives() {
		try {
			int waitTime = 5;
			crcValues[8] = 0;
			int errors = 0;
			while (crcValues[8] == 0) {
				String problem = "Unknown problem";
				drawLoadingText(20, "Connecting to web server");
				try {
					DataInputStream in = jaggrabRequest("crc" + (int) (Math.random() * 9.9999999E7) + "-" + 317);
					Buffer buffer = new Buffer(new byte[40]);
					in.readFully(buffer.payload, 0, 40);
					in.close();
					for (int crcValue = 0; crcValue < 9; crcValue++) {
						crcValues[crcValue] = buffer.getInt();
					}
					int servercrcValues = buffer.getInt();
					int clientCrcValues = 1234;
					for (int crcValue = 0; crcValue < 9; crcValue++) {
						clientCrcValues = (clientCrcValues << 1) + crcValues[crcValue];
					}
					if (servercrcValues != clientCrcValues) {
						problem = "checksum problem";
						crcValues[8] = 0;
					}
				} catch (java.io.EOFException eofexception) {
					problem = "EOF problem";
					crcValues[8] = 0;
				} catch (IOException ioexception) {
					problem = "connection problem";
					crcValues[8] = 0;
				} catch (Exception exception) {
					problem = "logic problem";
					crcValues[8] = 0;
					if (!SignLink.accessible) {
						break;
					}
				}
				if (crcValues[8] == 0) {
					errors++;
					for (int i_8_ = waitTime; i_8_ > 0; i_8_--) {
						if (errors >= 10) {
							drawLoadingText(10, "Game updated - please reload page");
							i_8_ = 10;
						} else {
							drawLoadingText(10, problem + " - Will retry in " + i_8_ + " secs.");
						}
						try {
							Thread.sleep(1000L);
						} catch (Exception exception) {
							/* empty */
						}
					}
					waitTime *= 2;
					if (waitTime > 60) {
						waitTime = 60;
					}
					aBoolean897 = !aBoolean897;
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("50895, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final boolean method17(int i, int i_9_) {
		try {
			if (i_9_ < 0) {
				return false;
			}
			int i_10_ = menuActionIndexes[i_9_];
			if (i != 9) {
				opcode = -1;
			}
			if (i_10_ >= 2000) {
				i_10_ -= 2000;
			}
			if (i_10_ == 337) {
				return true;
			}
			return false;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("93711, " + i + ", " + i_9_ + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method18() {
		try {
			aProducingGraphicsBuffer1191.createRasterizer();
			Rasterizer3D.lineOffsets = anIntArray1205;
			chatboxBackgroundImage.drawImage(0, 0);
			if (aBoolean1281) {
				fontBold.drawString(inputCustomMessage, 239, 40, 0);
				fontBold.drawString(chatMessage + "*", 239, 60, 128);
			} else if (inputType == 1) {
				fontBold.drawString("Enter amount:", 239, 40, 0);
				fontBold.drawString(inputInputMessage + "*", 239, 60, 128);
			} else if (inputType == 2) {
				fontBold.drawString("Enter name:", 239, 40, 0);
				fontBold.drawString(inputInputMessage + "*", 239, 60, 128);
			} else if (aString869 != null) {
				fontBold.drawString(aString869, 239, 40, 0);
				fontBold.drawString("Click to continue", 239, 60, 128);
			} else if (chatboxWidgetIndex != -1) {
				method105(0, 0, Widget.cache[chatboxWidgetIndex], 0);
			} else if (anInt1067 != -1) {
				method105(0, 0, Widget.cache[anInt1067], 0);
			} else {
				TypeFace typeface = fontNormal;
				int i_11_ = 0;
				Rasterizer.setCoordinates(0, 0, 463, 77);
				for (int i_12_ = 0; i_12_ < 100; i_12_++) {
					if (chatboxMessage[i_12_] != null) {
						int i_13_ = chatboxMessageType[i_12_];
						int i_14_ = 70 - i_11_ * 14 + anInt1114;
						String string = chatboxMessageName[i_12_];
						int i_15_ = 0;
						if (string != null && string.startsWith("@cr1@")) {
							string = string.substring(5);
							i_15_ = 1;
						}
						if (string != null && string.startsWith("@cr2@")) {
							string = string.substring(5);
							i_15_ = 2;
						}
						if (i_13_ == 0) {
							if (i_14_ > 0 && i_14_ < 110) {
								typeface.drawRegularString(chatboxMessage[i_12_], 4, i_14_, 0);
							}
							i_11_++;
						}
						if ((i_13_ == 1 || i_13_ == 2)
								&& (i_13_ == 1 || publicChatSetting == 0 || publicChatSetting == 1
										&& method109(false, string))) {
							if (i_14_ > 0 && i_14_ < 110) {
								int i_16_ = 4;
								if (i_15_ == 1) {
									anIndexedImageArray1244[0].drawImage(i_16_, i_14_ - 12);
									i_16_ += 14;
								}
								if (i_15_ == 2) {
									anIndexedImageArray1244[1].drawImage(i_16_, i_14_ - 12);
									i_16_ += 14;
								}
								typeface.drawRegularString(string + ":", i_16_, i_14_, 0);
								i_16_ += typeface.getStringEffectWidth(string) + 8;
								typeface.drawRegularString(chatboxMessage[i_12_], i_16_, i_14_, 255);
							}
							i_11_++;
						}
						if ((i_13_ == 3 || i_13_ == 7)
								&& anInt1220 == 0
								&& (i_13_ == 7 || privateChatSetting == 0 || privateChatSetting == 1
										&& method109(false, string))) {
							if (i_14_ > 0 && i_14_ < 110) {
								int i_17_ = 4;
								typeface.drawRegularString("From", i_17_, i_14_, 0);
								i_17_ += typeface.getStringEffectWidth("From ");
								if (i_15_ == 1) {
									anIndexedImageArray1244[0].drawImage(i_17_, i_14_ - 12);
									i_17_ += 14;
								}
								if (i_15_ == 2) {
									anIndexedImageArray1244[1].drawImage(i_17_, i_14_ - 12);
									i_17_ += 14;
								}
								typeface.drawRegularString(string + ":", i_17_, i_14_, 0);
								i_17_ += typeface.getStringEffectWidth(string) + 8;
								typeface.drawRegularString(chatboxMessage[i_12_], i_17_, i_14_, 8388608);
							}
							i_11_++;
						}
						if (i_13_ == 4 && (tradeSetting == 0 || tradeSetting == 1 && method109(false, string))) {
							if (i_14_ > 0 && i_14_ < 110) {
								typeface.drawRegularString(string + " " + chatboxMessage[i_12_], 4, i_14_, 8388736);
							}
							i_11_++;
						}
						if (i_13_ == 5 && anInt1220 == 0 && privateChatSetting < 2) {
							if (i_14_ > 0 && i_14_ < 110) {
								typeface.drawRegularString(chatboxMessage[i_12_], 4, i_14_, 8388608);
							}
							i_11_++;
						}
						if (i_13_ == 6 && anInt1220 == 0 && privateChatSetting < 2) {
							if (i_14_ > 0 && i_14_ < 110) {
								typeface.drawRegularString("To " + string + ":", 4, i_14_, 0);
								typeface.drawRegularString(chatboxMessage[i_12_],
										12 + typeface.getStringEffectWidth("To " + string), i_14_, 8388608);
							}
							i_11_++;
						}
						if (i_13_ == 8 && (tradeSetting == 0 || tradeSetting == 1 && method109(false, string))) {
							if (i_14_ > 0 && i_14_ < 110) {
								typeface.drawRegularString(string + " " + chatboxMessage[i_12_], 4, i_14_, 8270336);
							}
							i_11_++;
						}
					}
				}
				Rasterizer.resetCoordinates();
				anInt1236 = i_11_ * 14 + 7;
				if (anInt1236 < 78) {
					anInt1236 = 78;
				}
				method30(77, anInt1236 - anInt1114 - 77, 0, 463, anInt1236);
				String string;
				if (Client.localPlayer != null && Client.localPlayer.playerName != null) {
					string = Client.localPlayer.playerName;
				} else {
					string = TextUtils.formatName(aString1198);
				}
				typeface.drawRegularString(string + ":", 4, 90, 0);
				typeface.drawRegularString(chatboxInput + "*", 6 + typeface.getStringEffectWidth(string + ": "), 90,
						255);
				Rasterizer.drawHorizontalLine(0, 77, 479, 0);
			}
			if (actionMenuOpen && anInt973 == 2) {
				method40();
			}
			aProducingGraphicsBuffer1191.drawGraphics(17, 357, gameGraphics);
			currentSceneBuffer.createRasterizer();
			Rasterizer3D.lineOffsets = anIntArray1207;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("19689, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	@Override
	public final void init() {
		Client.nodeIndex = Integer.parseInt(getParameter("nodeid"));
		Client.portOffset = Integer.parseInt(getParameter("portoff"));
		String memoryType = getParameter("lowmem");
		if (memoryType != null && memoryType.equals("1")) {
			Client.setLowMemory();
		} else {
			Client.setHighMemory();
		}
		String worldType = getParameter("free");
		if (worldType != null && worldType.equals("1")) {
			Client.membersWorld = false;
		} else {
			Client.membersWorld = true;
		}
		this.initializeApplet(765, 503);
	}

	@Override
	public final void startRunnable(Runnable runnable, int i) {
		if (i > 10) {
			i = 10;
		}
		if (SignLink.applet != null) {
			SignLink.startRunnable(runnable, i);
		} else {
			super.startRunnable(runnable, i);
		}
	}

	public final Socket createSocket(int i) throws IOException {
		if (SignLink.applet != null) {
			return SignLink.openPort(i);
		}
		return new Socket(InetAddress.getByName(getCodeBase().getHost()), i);
	}

	public final void method20() {
		try {
			if (anInt1111 == 0) {
				int type = clickType;
				if (anInt1161 == 1 && clickX >= 516 && clickY >= 160 && clickX <= 765 && clickY <= 205) {
					type = 0;
				}
				if (actionMenuOpen) {
					if (type != 1) {
						int eventX = mouseEventX;
						int eventY = mouseEventY;
						if (anInt973 == 0) {
							eventX -= 4;
							eventY -= 4;
						}
						if (anInt973 == 1) {
							eventX -= 553;
							eventY -= 205;
						}
						if (anInt973 == 2) {
							eventX -= 17;
							eventY -= 357;
						}
						if (eventX < anInt974 - 10 || eventX > anInt974 + anInt976 + 10 || eventY < anInt975 - 10
								|| eventY > anInt975 + anInt977 + 10) {
							actionMenuOpen = false;
							if (anInt973 == 1) {
								drawTabArea = true;
							}
							if (anInt973 == 2) {
								aBoolean1248 = true;
							}
						}
					}
					if (type == 1) {
						int menuX = anInt974;
						int height = anInt975;
						int width = anInt976;
						int x = clickX;
						int y = clickY;
						if (anInt973 == 0) {
							x -= 4;
							y -= 4;
						}
						if (anInt973 == 1) {
							x -= 553;
							y -= 205;
						}
						if (anInt973 == 2) {
							x -= 17;
							y -= 357;
						}
						int hoveredRow = -1;
						for (int row = 0; row < menuActionRow; row++) {
							int rowY = height + 31 + (menuActionRow - 1 - row) * 15;
							if (x > menuX && x < menuX + width && y > rowY - 13 && y < rowY + 3) {
								hoveredRow = row;
							}
						}
						if (hoveredRow != -1) {
							processMenuAction(hoveredRow);
						}
						actionMenuOpen = false;
						if (anInt973 == 1) {
							drawTabArea = true;
						}
						if (anInt973 == 2) {
							aBoolean1248 = true;
						}
					}
				} else {
					if (type == 1 && menuActionRow > 0) {
						int i = menuActionIndexes[menuActionRow - 1];
						if (i == 632 || i == 78 || i == 867 || i == 431 || i == 53 || i == 74 || i == 454 || i == 539
								|| i == 493 || i == 847 || i == 447 || i == 1125) {
							int i_31_ = menuActionIndex2[menuActionRow - 1];
							int i_32_ = menuActionIndex3[menuActionRow - 1];
							Widget widget = Widget.cache[i_32_];
							if (widget.itemSwapable || widget.itemDeletesDraged) {
								aBoolean1267 = false;
								anInt1014 = 0;
								anInt1109 = i_32_;
								anInt1110 = i_31_;
								anInt1111 = 2;
								anInt1112 = clickX;
								anInt1113 = clickY;
								if (Widget.cache[i_32_].parentIndex == openWidgetIndex) {
									anInt1111 = 1;
								}
								if (Widget.cache[i_32_].parentIndex == chatboxWidgetIndex) {
									anInt1111 = 3;
								}
								return;
							}
						}
					}
					if (type == 1 && (anInt1278 == 1 || method17(9, menuActionRow - 1)) && menuActionRow > 2) {
						type = 2;
					}
					if (type == 1 && menuActionRow > 0) {
						processMenuAction(menuActionRow - 1);
					}
					if (type != 2 || menuActionRow <= 0) {
						return;
					}
					processMenuHovering();
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("37524, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method21(boolean bool, int i, byte[] bs) {
		do {
			try {
				SignLink.midiFade = bool ? 1 : 0;
				SignLink.writeMidi(bs, bs.length);
				if (i == 0) {
					break;
				}
				opcode = inBuffer.getUnsignedByte();
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("51005, " + bool + ", " + i + ", " + bs + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void method22() {
		try {
			try {
				lastSceneIndex = -1;
				aLinkedList1081.clear();
				projectileList.clear();
				Rasterizer3D.method363(Client.anInt871);
				method23(false);
				currentScene.method496(619);
				System.gc();
				for (int i = 0; i < 4; i++) {
					currentCollisionMap[i].reset();
				}
				for (int i = 0; i < 4; i++) {
					for (int i_33_ = 0; i_33_ < 104; i_33_++) {
						for (int i_34_ = 0; i_34_ < 104; i_34_++) {
							currentSceneTileFlags[i][i_33_][i_34_] = (byte) 0;
						}
					}
				}
				Region region = new Region(currentSceneTileFlags, 104, 104, anIntArrayArrayArray1239);
				int i = aByteArrayArray1208.length;
				outBuffer.putOpcode(0);
				if (!aBoolean1184) {
					for (int i_35_ = 0; i_35_ < i; i_35_++) {
						int i_36_ = (anIntArray1259[i_35_] >> 8) * 64 - regionAbsoluteBaseX;
						int i_37_ = (anIntArray1259[i_35_] & 0xff) * 64 - regionAbsoluteBaseY;
						byte[] bs = aByteArrayArray1208[i_35_];
						if (bs != null) {
							region.method462(bs, i_37_, i_36_, (anInt1094 - 6) * 8, (anInt1095 - 6) * 8, (byte) 4,
									currentCollisionMap);
						}
					}
					for (int i_38_ = 0; i_38_ < i; i_38_++) {
						int i_39_ = (anIntArray1259[i_38_] >> 8) * 64 - regionAbsoluteBaseX;
						int i_40_ = (anIntArray1259[i_38_] & 0xff) * 64 - regionAbsoluteBaseY;
						byte[] bs = aByteArrayArray1208[i_38_];
						if (bs == null && anInt1095 < 800) {
							region.initiateVertexHeights(i_39_, 64, i_40_, 64);
						}
					}
					Client.anInt1122++;
					if (Client.anInt1122 > 160) {
						Client.anInt1122 = 0;
						outBuffer.putOpcode(238);
						outBuffer.put(96);
					}
					outBuffer.putOpcode(0);
					for (int i_41_ = 0; i_41_ < i; i_41_++) {
						byte[] bs = aByteArrayArray1272[i_41_];
						if (bs != null) {
							int i_42_ = (anIntArray1259[i_41_] >> 8) * 64 - regionAbsoluteBaseX;
							int i_43_ = (anIntArray1259[i_41_] & 0xff) * 64 - regionAbsoluteBaseY;
							region.method472(i_42_, currentCollisionMap, i_43_, 7, currentScene, bs);
						}
					}
				}
				if (aBoolean1184) {
					for (int i_44_ = 0; i_44_ < 4; i_44_++) {
						for (int i_45_ = 0; i_45_ < 13; i_45_++) {
							for (int i_46_ = 0; i_46_ < 13; i_46_++) {
								int i_47_ = constructMapTiles[i_44_][i_45_][i_46_];
								if (i_47_ != -1) {
									int i_48_ = i_47_ >> 24 & 0x3;
									int i_49_ = i_47_ >> 1 & 0x3;
									int i_50_ = i_47_ >> 14 & 0x3ff;
									int i_51_ = i_47_ >> 3 & 0x7ff;
									int i_52_ = (i_50_ / 8 << 8) + i_51_ / 8;
									for (int i_53_ = 0; i_53_ < anIntArray1259.length; i_53_++) {
										if (anIntArray1259[i_53_] == i_52_ && aByteArrayArray1208[i_53_] != null) {
											region.method461(i_48_, i_49_, currentCollisionMap, 9, i_45_ * 8,
													(i_50_ & 0x7) * 8, aByteArrayArray1208[i_53_], (i_51_ & 0x7) * 8,
													i_44_, i_46_ * 8);
											break;
										}
									}
								}
							}
						}
					}
					for (int i_54_ = 0; i_54_ < 13; i_54_++) {
						for (int i_55_ = 0; i_55_ < 13; i_55_++) {
							int i_56_ = constructMapTiles[0][i_54_][i_55_];
							if (i_56_ == -1) {
								region.initiateVertexHeights(i_54_ * 8, 8, i_55_ * 8, 8);
							}
						}
					}
					outBuffer.putOpcode(0);
					for (int i_57_ = 0; i_57_ < 4; i_57_++) {
						for (int i_58_ = 0; i_58_ < 13; i_58_++) {
							for (int i_59_ = 0; i_59_ < 13; i_59_++) {
								int i_60_ = constructMapTiles[i_57_][i_58_][i_59_];
								if (i_60_ != -1) {
									int i_61_ = i_60_ >> 24 & 0x3;
									int i_62_ = i_60_ >> 1 & 0x3;
									int i_63_ = i_60_ >> 14 & 0x3ff;
									int i_64_ = i_60_ >> 3 & 0x7ff;
									int i_65_ = (i_63_ / 8 << 8) + i_64_ / 8;
									for (int i_66_ = 0; i_66_ < anIntArray1259.length; i_66_++) {
										if (anIntArray1259[i_66_] == i_65_ && aByteArrayArray1272[i_66_] != null) {
											region.method465(currentCollisionMap, currentScene, i_61_, i_58_ * 8,
													(i_64_ & 0x7) * 8, true, i_57_, aByteArrayArray1272[i_66_],
													(i_63_ & 0x7) * 8, i_62_, i_59_ * 8);
											break;
										}
									}
								}
							}
						}
					}
				}
				outBuffer.putOpcode(0);
				region.createRegionScene(currentCollisionMap, currentScene);
				currentSceneBuffer.createRasterizer();
				outBuffer.putOpcode(0);
				int i_67_ = Region.lowestPlane;
				if (i_67_ > currentSceneIndex) {
					i_67_ = currentSceneIndex;
				}
				if (i_67_ < currentSceneIndex - 1) {
					i_67_ = currentSceneIndex - 1;
				}
				if (Client.lowMemory) {
					currentScene.method497(Region.lowestPlane, -34686);
				} else {
					currentScene.method497(0, -34686);
				}
				for (int i_68_ = 0; i_68_ < 104; i_68_++) {
					for (int i_69_ = 0; i_69_ < 104; i_69_++) {
						method25(i_68_, i_69_);
					}
				}
				Client.anInt1076++;
				if (Client.anInt1076 > 98) {
					Client.anInt1076 = 0;
					outBuffer.putOpcode(150);
				}
				method63(-919);
			} catch (Exception exception) {
				/* empty */
			}
			GameObjectDefinition.modelCache.removeAll();
			if (gameFrame != null) {
				outBuffer.putOpcode(210);
				outBuffer.putInt(1057001181);
			}
			if (Client.lowMemory && SignLink.mainCache != null) {
				int i = onDemandRequester.fileCount(0);
				for (int i_70_ = 0; i_70_ < i; i_70_++) {
					int i_71_ = onDemandRequester.modelIndex(i_70_);
					if ((i_71_ & 0x79) == 0) {
						Model.method409(116, i_70_);
					}
				}
			}
			System.gc();
			Rasterizer3D.method364(20, true);
			onDemandRequester.clearPassiveRequests();
			int i = (anInt1094 - 6) / 8 - 1;
			int i_72_ = (anInt1094 + 6) / 8 + 1;
			int i_73_ = (anInt1095 - 6) / 8 - 1;
			int i_74_ = (anInt1095 + 6) / 8 + 1;
			if (aBoolean1166) {
				i = 49;
				i_72_ = 50;
				i_73_ = 49;
				i_74_ = 50;
			}
			for (int i_75_ = i; i_75_ <= i_72_; i_75_++) {
				for (int i_76_ = i_73_; i_76_ <= i_74_; i_76_++) {
					if (i_75_ == i || i_75_ == i_72_ || i_76_ == i_73_ || i_76_ == i_74_) {
						int i_77_ = onDemandRequester.regIndex(0, i_76_, i_75_);
						if (i_77_ != -1) {
							onDemandRequester.passiveRequest(i_77_, 3);
						}
						int i_78_ = onDemandRequester.regIndex(1, i_76_, i_75_);
						if (i_78_ != -1) {
							onDemandRequester.passiveRequest(i_78_, 3);
						}
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("81650, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method23(boolean bool) {
		try {
			GameObjectDefinition.modelCache.removeAll();
			GameObjectDefinition.modelCache2.removeAll();
			ActorDefinition.modelCache.removeAll();
			ItemDefinition.modelCache.removeAll();
			ItemDefinition.rgbImageCache.removeAll();
			if (bool) {
				opcode = -1;
			}
			Player.modelCache.removeAll();
			SpotAnimation.modelCache.removeAll();
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("63488, " + bool + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void renderViewport(int plane) {
		try {
			int[] pixels = minimapImage.pixels;
			int pixelAmount = pixels.length;
			for (int pixel = 0; pixel < pixelAmount; pixel++) {
				pixels[pixel] = 0;
			}
			for (int viewportY = 1; viewportY < 103; viewportY++) {
				int drawPoint = 24628 + (103 - viewportY) * 512 * 4;
				for (int viewportX = 1; viewportX < 103; viewportX++) {
					if ((currentSceneTileFlags[plane][viewportX][viewportY] & 0x18) == 0) {
						currentScene.renderMinimapDot(pixels, drawPoint, 512, plane, viewportX, viewportY);
					}
					if (plane < 3 && (currentSceneTileFlags[plane + 1][viewportX][viewportY] & 0x8) != 0) {
						currentScene.renderMinimapDot(pixels, drawPoint, 512, plane + 1, viewportX, viewportY);
					}
					drawPoint += 4;
				}
			}
			int primaryColour = (238 + (int) (Math.random() * 20.0) - 10 << 16)
					+ (238 + (int) (Math.random() * 20.0) - 10 << 8) + (238 + (int) (Math.random() * 20.0) - 10);
			int secondaryColour = 238 + (int) (Math.random() * 20.0) - 10 << 16;
			minimapImage.createRasterizer();
			for (int viewportY = 1; viewportY < 103; viewportY++) {
				for (int viewportX = 1; viewportX < 103; viewportX++) {
					if ((currentSceneTileFlags[plane][viewportX][viewportY] & 0x18) == 0) {
						renderCurrentScene(viewportY, -960, primaryColour, viewportX, secondaryColour, plane);
					}
					if (plane < 3 && (currentSceneTileFlags[plane + 1][viewportX][viewportY] & 0x8) != 0) {
						renderCurrentScene(viewportY, -960, primaryColour, viewportX, secondaryColour, plane + 1);
					}
				}
			}
			currentSceneBuffer.createRasterizer();

			minimapHintCount = 0;
			for (int viewportX = 0; viewportX < 104; viewportX++) {
				for (int viewportY = 0; viewportY < 104; viewportY++) {
					int floorHash = currentScene.getFloorHash(currentSceneIndex, viewportX, viewportY);
					if (floorHash != 0) {
						floorHash = floorHash >> 14 & 0x7fff;
						int icon = GameObjectDefinition.getDefinition(floorHash).icon;
						if (icon >= 0) {
							int drawPointX = viewportX;
							int drawPointY = viewportY;
							if (icon != 22 && icon != 29 && icon != 34 && icon != 36 && icon != 46 && icon != 47
									&& icon != 48) {
								int regionWidth = 104;
								int regionHeight = 104;
								int[][] flags = currentCollisionMap[currentSceneIndex].adjacency;
								for (int off = 0; off < 10; off++) {
									int randPlane = (int) (Math.random() * 4.0);
									if (randPlane == 0 && drawPointX > 0 && drawPointX > viewportX - 3
											&& (flags[drawPointX - 1][drawPointY] & 0x1280108) == 0) {
										drawPointX--;
									}
									if (randPlane == 1 && drawPointX < regionWidth - 1 && drawPointX < viewportX + 3
											&& (flags[drawPointX + 1][drawPointY] & 0x1280180) == 0) {
										drawPointX++;
									}
									if (randPlane == 2 && drawPointY > 0 && drawPointY > viewportY - 3
											&& (flags[drawPointX][drawPointY - 1] & 0x1280102) == 0) {
										drawPointY--;
									}
									if (randPlane == 3 && drawPointY < regionHeight - 1 && drawPointY < viewportY + 3
											&& (flags[drawPointX][drawPointY + 1] & 0x1280120) == 0) {
										drawPointY++;
									}
								}
							}
							minimapHint[minimapHintCount] = worldMapHintIcons[icon];
							minimapHintX[minimapHintCount] = drawPointX;
							minimapHintY[minimapHintCount] = drawPointY;
							minimapHintCount++;
						}
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("35531, " + plane + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method25(int i, int i_99_) {
		LinkedList linkedlist = groundItemNodes[currentSceneIndex][i][i_99_];
		if (linkedlist == null) {
			currentScene.method517(currentSceneIndex, i, i_99_);
		} else {
			int i_100_ = -99999999;
			Item item = null;
			for (Item item_101_ = (Item) linkedlist.getBack(); item_101_ != null; item_101_ = (Item) linkedlist
					.getPrevious()) {
				ItemDefinition itemdefinition = ItemDefinition.getDefinition(item_101_.itemId);
				int i_102_ = itemdefinition.value;
				if (itemdefinition.stackable) {
					i_102_ *= item_101_.itemCount + 1;
				}
				if (i_102_ > i_100_) {
					i_100_ = i_102_;
					item = item_101_;
				}
			}
			linkedlist.insertFront(item);
			Item item_103_ = null;
			Item item_104_ = null;
			for (Item item_105_ = (Item) linkedlist.getBack(); item_105_ != null; item_105_ = (Item) linkedlist
					.getPrevious()) {
				if (item_105_.itemId != item.itemId && item_103_ == null) {
					item_103_ = item_105_;
				}
				if (item_105_.itemId != item.itemId && item_105_.itemId != item_103_.itemId && item_104_ == null) {
					item_104_ = item_105_;
				}
			}
			int i_106_ = i + (i_99_ << 7) + 1610612736;
			currentScene.method503((byte) 7, i, i_106_, item_103_,
					method42(currentSceneIndex, i_99_ * 128 + 64, true, i * 128 + 64), item_104_, item,
					currentSceneIndex, i_99_);
		}
	}

	public final void method26(boolean bool) {
		try {
			for (int index = 0; index < actorCount; index++) {
				Npc npc = actors[anIntArray862[index]];
				int i_108_ = 536870912 + (anIntArray862[index] << 14);
				if (npc != null && npc.isVisibile() && npc.npcDefinition.visible == bool) {
					int i_109_ = npc.xWithBoundary >> 7;
					int i_110_ = npc.yWithBoundary >> 7;
					if (i_109_ >= 0 && i_109_ < 104 && i_110_ >= 0 && i_110_ < 104) {
						if (npc.boundaryDimension == 1 && (npc.xWithBoundary & 0x7f) == 64
								&& (npc.yWithBoundary & 0x7f) == 64) {
							if (anIntArrayArray954[i_109_][i_110_] == anInt1290) {
								continue;
							}
							anIntArrayArray954[i_109_][i_110_] = anInt1290;
						}
						if (!npc.npcDefinition.clickable) {
							i_108_ += -2147483648;
						}
						currentScene.method507(currentSceneIndex, npc.anInt1572, (byte) 6,
								method42(currentSceneIndex, npc.yWithBoundary, true, npc.xWithBoundary), i_108_,
								npc.yWithBoundary, (npc.boundaryDimension - 1) * 64 + 60, npc.xWithBoundary, npc,
								npc.aBoolean1561);
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("19775, " + bool + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	private final void method28(String string) {
		System.out.println(string);
		try {
			getAppletContext().showDocument(new URL(getCodeBase(), "loaderror_" + string + ".html"));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		for (;;) {
			try {
				Thread.sleep(1000L);
			} catch (Exception exception) {
				/* empty */
			}
		}
	}

	public final void processClickingWidget(int i, Widget widget, int i_112_, int i_113_, int i_114_, int i_115_) {
		try {
			if (widget.type == 0
					&& widget.children != null
					&& !widget.hiddenUntilHovered
					&& (i_112_ >= i && i_114_ >= i_113_ && i_112_ <= i + widget.width && i_114_ <= i_113_
							+ widget.height)) {
				int i_116_ = widget.children.length;
				for (int i_117_ = 0; i_117_ < i_116_; i_117_++) {
					int i_118_ = widget.childrenX[i_117_] + i;
					int i_119_ = widget.childrenY[i_117_] + i_113_ - i_115_;
					Widget widget_120_ = Widget.cache[widget.children[i_117_]];
					i_118_ += widget_120_.x;
					i_119_ += widget_120_.y;
					if ((widget_120_.hoveredPopup >= 0 || widget_120_.disabledHoveredColor != 0) && i_112_ >= i_118_
							&& i_114_ >= i_119_ && i_112_ < i_118_ + widget_120_.width
							&& i_114_ < i_119_ + widget_120_.height) {
						if (widget_120_.hoveredPopup >= 0) {
							anInt911 = widget_120_.hoveredPopup;
						} else {
							anInt911 = widget_120_.index;
						}
					}
					if (widget_120_.type == 0) {
						processClickingWidget(i_118_, widget_120_, i_112_, i_119_, i_114_, widget_120_.scrollPosition);
						if (widget_120_.scrollLimit > widget_120_.height) {
							method65(i_118_ + widget_120_.width, widget_120_.height, i_112_, i_114_, widget_120_,
									i_119_, true, widget_120_.scrollLimit, 0);
						}
					} else {
						if (widget_120_.actionType == 1 && i_112_ >= i_118_ && i_114_ >= i_119_
								&& i_112_ < i_118_ + widget_120_.width && i_114_ < i_119_ + widget_120_.height) {
							boolean bool = false;
							if (widget_120_.contentType != 0) {
								bool = method103(widget_120_, false);
							}
							if (!bool) {
								menuActionNames[menuActionRow] = widget_120_.tooltip;
								menuActionIndexes[menuActionRow] = 315;
								menuActionIndex3[menuActionRow] = widget_120_.index;
								menuActionRow++;
							}
						}
						if (widget_120_.actionType == 2 && anInt1161 == 0 && i_112_ >= i_118_ && i_114_ >= i_119_
								&& i_112_ < i_118_ + widget_120_.width && i_114_ < i_119_ + widget_120_.height) {
							String string = widget_120_.selectedActionName;
							if (string.indexOf(" ") != -1) {
								string = string.substring(0, string.indexOf(" "));
							}
							menuActionNames[menuActionRow] = string + " @gre@" + widget_120_.spellName;
							menuActionIndexes[menuActionRow] = 626;
							menuActionIndex3[menuActionRow] = widget_120_.index;
							menuActionRow++;
						}
						if (widget_120_.actionType == 3 && i_112_ >= i_118_ && i_114_ >= i_119_
								&& i_112_ < i_118_ + widget_120_.width && i_114_ < i_119_ + widget_120_.height) {
							menuActionNames[menuActionRow] = "Close";
							menuActionIndexes[menuActionRow] = 200;
							menuActionIndex3[menuActionRow] = widget_120_.index;
							menuActionRow++;
						}
						if (widget_120_.actionType == 4 && i_112_ >= i_118_ && i_114_ >= i_119_
								&& i_112_ < i_118_ + widget_120_.width && i_114_ < i_119_ + widget_120_.height) {
							menuActionNames[menuActionRow] = widget_120_.tooltip;
							menuActionIndexes[menuActionRow] = 169;
							menuActionIndex3[menuActionRow] = widget_120_.index;
							menuActionRow++;
						}
						if (widget_120_.actionType == 5 && i_112_ >= i_118_ && i_114_ >= i_119_
								&& i_112_ < i_118_ + widget_120_.width && i_114_ < i_119_ + widget_120_.height) {
							menuActionNames[menuActionRow] = widget_120_.tooltip;
							menuActionIndexes[menuActionRow] = 646;
							menuActionIndex3[menuActionRow] = widget_120_.index;
							menuActionRow++;
						}
						if (widget_120_.actionType == 6 && aBoolean1174 == false && i_112_ >= i_118_
								&& i_114_ >= i_119_ && i_112_ < i_118_ + widget_120_.width
								&& i_114_ < i_119_ + widget_120_.height) {
							menuActionNames[menuActionRow] = widget_120_.tooltip;
							menuActionIndexes[menuActionRow] = 679;
							menuActionIndex3[menuActionRow] = widget_120_.index;
							menuActionRow++;
						}
						if (widget_120_.type == 2) {
							int i_121_ = 0;
							for (int i_122_ = 0; i_122_ < widget_120_.height; i_122_++) {
								for (int i_123_ = 0; i_123_ < widget_120_.width; i_123_++) {
									int i_124_ = i_118_ + i_123_ * (32 + widget_120_.itemSpritePadsX);
									int i_125_ = i_119_ + i_122_ * (32 + widget_120_.itemSpritePadsY);
									if (i_121_ < 20) {
										i_124_ += widget_120_.imageX[i_121_];
										i_125_ += widget_120_.imageY[i_121_];
									}
									if (i_112_ >= i_124_ && i_114_ >= i_125_ && i_112_ < i_124_ + 32
											&& i_114_ < i_125_ + 32) {
										anInt1091 = i_121_;
										anInt1092 = widget_120_.index;
										if (widget_120_.items[i_121_] > 0) {
											ItemDefinition itemdefinition = ItemDefinition
													.getDefinition(widget_120_.items[i_121_] - 1);
											if (anInt1307 == 1 && widget_120_.isInventory) {
												if (widget_120_.index != anInt1309 || i_121_ != anInt1308) {
													menuActionNames[menuActionRow] = "Use " + aString1311
															+ " with @lre@" + itemdefinition.name;
													menuActionIndexes[menuActionRow] = 870;
													menuActionIndex1[menuActionRow] = itemdefinition.index;
													menuActionIndex2[menuActionRow] = i_121_;
													menuActionIndex3[menuActionRow] = widget_120_.index;
													menuActionRow++;
												}
											} else if (anInt1161 == 1 && widget_120_.isInventory) {
												if ((anInt1163 & 0x10) == 16) {
													menuActionNames[menuActionRow] = aString1164 + " @lre@"
															+ itemdefinition.name;
													menuActionIndexes[menuActionRow] = 543;
													menuActionIndex1[menuActionRow] = itemdefinition.index;
													menuActionIndex2[menuActionRow] = i_121_;
													menuActionIndex3[menuActionRow] = widget_120_.index;
													menuActionRow++;
												}
											} else {
												if (widget_120_.isInventory) {
													for (int i_126_ = 4; i_126_ >= 3; i_126_--) {
														if (itemdefinition.inventoryActions != null
																&& itemdefinition.inventoryActions[i_126_] != null) {
															menuActionNames[menuActionRow] = itemdefinition.inventoryActions[i_126_]
																	+ " @lre@" + itemdefinition.name;
															if (i_126_ == 3) {
																menuActionIndexes[menuActionRow] = 493;
															}
															if (i_126_ == 4) {
																menuActionIndexes[menuActionRow] = 847;
															}
															menuActionIndex1[menuActionRow] = itemdefinition.index;
															menuActionIndex2[menuActionRow] = i_121_;
															menuActionIndex3[menuActionRow] = widget_120_.index;
															menuActionRow++;
														} else if (i_126_ == 4) {
															menuActionNames[menuActionRow] = "Drop @lre@"
																	+ itemdefinition.name;
															menuActionIndexes[menuActionRow] = 847;
															menuActionIndex1[menuActionRow] = itemdefinition.index;
															menuActionIndex2[menuActionRow] = i_121_;
															menuActionIndex3[menuActionRow] = widget_120_.index;
															menuActionRow++;
														}
													}
												}
												if (widget_120_.itemUsable) {
													menuActionNames[menuActionRow] = "Use @lre@" + itemdefinition.name;
													menuActionIndexes[menuActionRow] = 447;
													menuActionIndex1[menuActionRow] = itemdefinition.index;
													menuActionIndex2[menuActionRow] = i_121_;
													menuActionIndex3[menuActionRow] = widget_120_.index;
													menuActionRow++;
												}
												if (widget_120_.isInventory && itemdefinition.inventoryActions != null) {
													for (int i_127_ = 2; i_127_ >= 0; i_127_--) {
														if (itemdefinition.inventoryActions[i_127_] != null) {
															menuActionNames[menuActionRow] = itemdefinition.inventoryActions[i_127_]
																	+ " @lre@" + itemdefinition.name;
															if (i_127_ == 0) {
																menuActionIndexes[menuActionRow] = 74;
															}
															if (i_127_ == 1) {
																menuActionIndexes[menuActionRow] = 454;
															}
															if (i_127_ == 2) {
																menuActionIndexes[menuActionRow] = 539;
															}
															menuActionIndex1[menuActionRow] = itemdefinition.index;
															menuActionIndex2[menuActionRow] = i_121_;
															menuActionIndex3[menuActionRow] = widget_120_.index;
															menuActionRow++;
														}
													}
												}
												if (widget_120_.actions != null) {
													for (int i_128_ = 4; i_128_ >= 0; i_128_--) {
														if (widget_120_.actions[i_128_] != null) {
															menuActionNames[menuActionRow] = widget_120_.actions[i_128_]
																	+ " @lre@" + itemdefinition.name;
															if (i_128_ == 0) {
																menuActionIndexes[menuActionRow] = 632;
															}
															if (i_128_ == 1) {
																menuActionIndexes[menuActionRow] = 78;
															}
															if (i_128_ == 2) {
																menuActionIndexes[menuActionRow] = 867;
															}
															if (i_128_ == 3) {
																menuActionIndexes[menuActionRow] = 431;
															}
															if (i_128_ == 4) {
																menuActionIndexes[menuActionRow] = 53;
															}
															menuActionIndex1[menuActionRow] = itemdefinition.index;
															menuActionIndex2[menuActionRow] = i_121_;
															menuActionIndex3[menuActionRow] = widget_120_.index;
															menuActionRow++;
														}
													}
												}
												menuActionNames[menuActionRow] = "Examine @lre@" + itemdefinition.name;
												menuActionIndexes[menuActionRow] = 1125;
												menuActionIndex1[menuActionRow] = itemdefinition.index;
												menuActionIndex2[menuActionRow] = i_121_;
												menuActionIndex3[menuActionRow] = widget_120_.index;
												menuActionRow++;
											}
										}
									}
									i_121_++;
								}
							}
						}
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("38948, " + i + ", " + widget + ", " + i_112_ + ", " + i_113_ + ", " + i_114_ + ", "
					+ i_115_ + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method30(int height, int i_130_, int y, int x, int i_133_) {
		scrollbarUp.drawImage(x, y);
		scrollbarDown.drawImage(x, y + height - 16);
		Rasterizer.drawFilledRectangle(x, y + 16, 16, height - 32, anInt1027);
		int i_134_ = (height - 32) * height / i_133_;
		if (i_134_ < 8) {
			i_134_ = 8;
		}
		int i_135_ = (height - 32 - i_134_) * i_130_ / (i_133_ - height);
		Rasterizer.drawFilledRectangle(x, y + 16 + i_135_, 16, i_134_, anInt1088);
		Rasterizer.drawVerticalLine(x, y + 16 + i_135_, i_134_, anInt927);
		Rasterizer.drawVerticalLine(x + 1, y + 16 + i_135_, i_134_, anInt927);
		Rasterizer.drawHorizontalLine(x, y + 16 + i_135_, 16, anInt927);
		Rasterizer.drawHorizontalLine(x, y + 17 + i_135_, 16, anInt927);
		Rasterizer.drawVerticalLine(x + 15, y + 16 + i_135_, i_134_, anInt952);
		Rasterizer.drawVerticalLine(x + 14, y + 17 + i_135_, i_134_ - 1, anInt952);
		Rasterizer.drawHorizontalLine(x, y + 15 + i_135_ + i_134_, 16, anInt952);
		Rasterizer.drawHorizontalLine(x + 1, y + 14 + i_135_ + i_134_, 15, anInt952);
	}

	private final void updateActors(Buffer buffer, int actorIndex) {
		try {
			anInt864 = 0;
			anInt918 = 0;
			method139(buffer, -45, actorIndex);
			method46(actorIndex, buffer, (byte) 2);
			method86(actorIndex, buffer, true);
			for (int i_137_ = 0; i_137_ < anInt864; i_137_++) {
				int i_138_ = anIntArray865[i_137_];
				if (actors[i_138_].anInt1557 != Client.currentCycle) {
					actors[i_138_].npcDefinition = null;
					actors[i_138_] = null;
				}
			}
			if (buffer.offset != actorIndex) {
				SignLink.reportError(aString1198 + " size mismatch in getnpcpos - pos:" + buffer.offset + " psize:"
						+ actorIndex);
				throw new RuntimeException("eek");
			}
			for (int i_139_ = 0; i_139_ < actorCount; i_139_++) {
				if (actors[anIntArray862[i_139_]] == null) {
					SignLink.reportError(aString1198 + " null entry in npc list - pos:" + i_139_ + " size:"
							+ actorCount);
					throw new RuntimeException("eek");
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("78640, " + buffer + ", " + actorIndex + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void processClickingChatSettings() {
		try {
			if (clickType != 1) {
				return;
			}
			if (clickX >= 6 && clickX <= 106 && clickY >= 467 && clickY <= 499) {
				publicChatSetting = (publicChatSetting + 1) % 4;
				aBoolean1258 = true;
				aBoolean1248 = true;
				outBuffer.putOpcode(95);
				outBuffer.put(publicChatSetting);
				outBuffer.put(privateChatSetting);
				outBuffer.put(tradeSetting);
			}
			if (clickX >= 135 && clickX <= 235 && clickY >= 467 && clickY <= 499) {
				privateChatSetting = (privateChatSetting + 1) % 3;
				aBoolean1258 = true;
				aBoolean1248 = true;
				outBuffer.putOpcode(95);
				outBuffer.put(publicChatSetting);
				outBuffer.put(privateChatSetting);
				outBuffer.put(tradeSetting);
			}
			if (clickX >= 273 && clickX <= 373 && clickY >= 467 && clickY <= 499) {
				tradeSetting = (tradeSetting + 1) % 3;
				aBoolean1258 = true;
				aBoolean1248 = true;
				outBuffer.putOpcode(95);
				outBuffer.put(publicChatSetting);
				outBuffer.put(privateChatSetting);
				outBuffer.put(tradeSetting);
			}
			if (clickX >= 412 && clickX <= 512 && clickY >= 467 && clickY <= 499) {
				if (openWidgetIndex == -1) {
					closeWidgets();
					reportWidgetName = "";
					aBoolean1183 = false;
					for (int i = 0; i < Widget.cache.length; i++) {
						if (Widget.cache[i] != null && Widget.cache[i].contentType == 600) {
							anInt1203 = openWidgetIndex = Widget.cache[i].parentIndex;
							break;
						}
					}
				} else {
					sendMessage("Please close the interface you have open before using 'report abuse'", 0, "");
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("30699, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method33(int i) {
		do {
			int i_140_ = Varp.aVarpArray746[i].anInt754;
			if (i_140_ != 0) {
				int i_141_ = settings[i];
				if (i_140_ == 1) {
					if (i_141_ == 1) {
						Rasterizer3D.method369(0.9);
					}
					if (i_141_ == 2) {
						Rasterizer3D.method369(0.8);
					}
					if (i_141_ == 3) {
						Rasterizer3D.method369(0.7);
					}
					if (i_141_ == 4) {
						Rasterizer3D.method369(0.6);
					}
					ItemDefinition.rgbImageCache.removeAll();
					drawGameframe = true;
				}
				if (i_140_ == 3) {
					if (i_141_ == 0) {
						setMidiVolume(musicEnabled, 0);
						musicEnabled = true;
					}
					if (i_141_ == 1) {
						setMidiVolume(musicEnabled, -400);
						musicEnabled = true;
					}
					if (i_141_ == 2) {
						setMidiVolume(musicEnabled, -800);
						musicEnabled = true;
					}
					if (i_141_ == 3) {
						setMidiVolume(musicEnabled, -1200);
						musicEnabled = true;
					}
					if (i_141_ == 4) {
						musicEnabled = false;
					}
					if (!Client.lowMemory) {
						if (musicEnabled) {
							onDemandRequesterId = songIndex;
							midiFade = true;
							onDemandRequester.request(2, onDemandRequesterId);
						} else {
							stopMusic();
						}
						songFadeCycle = 0;
					}
				}
				if (i_140_ == 4) {
					if (i_141_ == 0) {
						aBoolean873 = true;
						setWaveVolume(0);
					}
					if (i_141_ == 1) {
						aBoolean873 = true;
						setWaveVolume(-400);
					}
					if (i_141_ == 2) {
						aBoolean873 = true;
						setWaveVolume(-800);
					}
					if (i_141_ == 3) {
						aBoolean873 = true;
						setWaveVolume(-1200);
					}
					if (i_141_ == 4) {
						aBoolean873 = false;
					}
				}
				if (i_140_ == 5) {
					anInt1278 = i_141_;
				}
				if (i_140_ == 6) {
					anInt1274 = i_141_;
				}
				if (i_140_ == 8) {
					anInt1220 = i_141_;
					aBoolean1248 = true;
				}
				if (i_140_ != 9) {
					break;
				}
				anInt938 = i_141_;
			}
			break;
		} while (false);
	}

	public final void method34() {
		try {
			anInt999 = 0;
			for (int i_143_ = -1; i_143_ < playerCount + actorCount; i_143_++) {
				Actor actor;
				if (i_143_ == -1) {
					actor = Client.localPlayer;
				} else if (i_143_ < playerCount) {
					actor = players[anIntArray917[i_143_]];
				} else {
					actor = actors[anIntArray862[i_143_ - playerCount]];
				}
				if (actor != null && actor.isVisibile()) {
					if (actor instanceof Npc) {
						ActorDefinition npcdefinition = ((Npc) actor).npcDefinition;
						if (npcdefinition.childrenIndexes != null) {
							npcdefinition = npcdefinition.getChildDefinition();
						}
						if (npcdefinition == null) {
							continue;
						}
					}
					if (i_143_ < playerCount) {
						int i_144_ = 30;
						Player player = (Player) actor;
						if (player.headIcon != 0) {
							method127(true, actor, actor.modelHeight + 15);
							if (anInt988 > -1) {
								for (int i_145_ = 0; i_145_ < 8; i_145_++) {
									if ((player.headIcon & 1 << i_145_) != 0) {
										anImageRGBArray1120[i_145_].drawImage(anInt988 - 12, anInt989 - i_144_);
										i_144_ -= 25;
									}
								}
							}
						}
						if (i_143_ >= 0 && hintIconType == 10 && hintIconPlayerIndex == anIntArray917[i_143_]) {
							method127(true, actor, actor.modelHeight + 15);
							if (anInt988 > -1) {
								anImageRGBArray1120[7].drawImage(anInt988 - 12, anInt989 - i_144_);
							}
						}
					} else {
						ActorDefinition npcdefinition = ((Npc) actor).npcDefinition;
						if (npcdefinition.headIcon >= 0 && npcdefinition.headIcon < anImageRGBArray1120.length) {
							method127(true, actor, actor.modelHeight + 15);
							if (anInt988 > -1) {
								anImageRGBArray1120[npcdefinition.headIcon].drawImage(anInt988 - 12, anInt989 - 30);
							}
						}
						if (hintIconType == 1 && hintIconActorIndex == anIntArray862[i_143_ - playerCount]
								&& Client.currentCycle % 20 < 10) {
							method127(true, actor, actor.modelHeight + 15);
							if (anInt988 > -1) {
								anImageRGBArray1120[2].drawImage(anInt988 - 12, anInt989 - 28);
							}
						}
					}
					if (actor.forcedChat != null
							&& (i_143_ >= playerCount || publicChatSetting == 0 || publicChatSetting == 3 || publicChatSetting == 1
									&& method109(false, ((Player) actor).playerName))) {
						method127(true, actor, actor.modelHeight);
						if (anInt988 > -1 && anInt999 < anInt1000) {
							anIntArray1004[anInt999] = fontBold.getStringWidth(actor.forcedChat) / 2;
							anIntArray1003[anInt999] = fontBold.characterDefaultHeight;
							anIntArray1001[anInt999] = anInt988;
							anIntArray1002[anInt999] = anInt989;
							anIntArray1005[anInt999] = actor.chatColor;
							anIntArray1006[anInt999] = actor.chatEffect;
							anIntArray1007[anInt999] = actor.anInt1555;
							aStringArray1008[anInt999++] = actor.forcedChat;
							if (anInt1274 == 0 && actor.chatEffect >= 1 && actor.chatEffect <= 3) {
								anIntArray1003[anInt999] += 10;
								anIntArray1002[anInt999] += 5;
							}
							if (anInt1274 == 0 && actor.chatEffect == 4) {
								anIntArray1004[anInt999] = 60;
							}
							if (anInt1274 == 0 && actor.chatEffect == 5) {
								anIntArray1003[anInt999] += 5;
							}
						}
					}
					if (actor.endCycle > Client.currentCycle) {
						method127(true, actor, actor.modelHeight + 15);
						if (anInt988 > -1) {
							int i_146_ = actor.maxHealth * 30 / actor.currentHealth;
							if (i_146_ > 30) {
								i_146_ = 30;
							}
							Rasterizer.drawFilledRectangle(anInt988 - 15, anInt989 - 3, i_146_, 5, 65280);
							Rasterizer.drawFilledRectangle(anInt988 - 15 + i_146_, anInt989 - 3, 30 - i_146_, 5,
									16711680);
						}
					}
					for (int i_147_ = 0; i_147_ < 4; i_147_++) {
						if (actor.hitCycles[i_147_] > Client.currentCycle) {
							method127(true, actor, actor.modelHeight / 2);
							if (anInt988 > -1) {
								if (i_147_ == 1) {
									anInt989 -= 20;
								}
								if (i_147_ == 2) {
									anInt988 -= 15;
									anInt989 -= 10;
								}
								if (i_147_ == 3) {
									anInt988 += 15;
									anInt989 -= 10;
								}
								anImageRGBArray1012[actor.hitTypes[i_147_]].drawImage(anInt988 - 12, anInt989 - 12);
								fontSmall.drawString(String.valueOf(actor.hitDamages[i_147_]), anInt988, anInt989 + 4,
										0);
								fontSmall.drawString(String.valueOf(actor.hitDamages[i_147_]), anInt988 - 1,
										anInt989 + 3, 0xFFFFFF);
							}
						}
					}
				}
			}
			for (int i_148_ = 0; i_148_ < anInt999; i_148_++) {
				int i_149_ = anIntArray1001[i_148_];
				int i_150_ = anIntArray1002[i_148_];
				int i_151_ = anIntArray1004[i_148_];
				int i_152_ = anIntArray1003[i_148_];
				boolean bool = true;
				while (bool) {
					bool = false;
					for (int i_153_ = 0; i_153_ < i_148_; i_153_++) {
						if (i_150_ + 2 > anIntArray1002[i_153_] - anIntArray1003[i_153_]
								&& i_150_ - i_152_ < anIntArray1002[i_153_] + 2
								&& i_149_ - i_151_ < anIntArray1001[i_153_] + anIntArray1004[i_153_]
								&& i_149_ + i_151_ > anIntArray1001[i_153_] - anIntArray1004[i_153_]
								&& anIntArray1002[i_153_] - anIntArray1003[i_153_] < i_150_) {
							i_150_ = anIntArray1002[i_153_] - anIntArray1003[i_153_];
							bool = true;
						}
					}
				}
				anInt988 = anIntArray1001[i_148_];
				anInt989 = anIntArray1002[i_148_] = i_150_;
				String string = aStringArray1008[i_148_];
				if (anInt1274 == 0) {
					int i_154_ = 0xFFFF00;
					if (anIntArray1005[i_148_] < 6) {
						i_154_ = anIntArray990[anIntArray1005[i_148_]];
					}
					if (anIntArray1005[i_148_] == 6) {
						i_154_ = anInt1290 % 20 < 10 ? 16711680 : 0xFFFF00;
					}
					if (anIntArray1005[i_148_] == 7) {
						i_154_ = anInt1290 % 20 < 10 ? 255 : 65535;
					}
					if (anIntArray1005[i_148_] == 8) {
						i_154_ = anInt1290 % 20 < 10 ? 45056 : 8454016;
					}
					if (anIntArray1005[i_148_] == 9) {
						int i_155_ = 150 - anIntArray1007[i_148_];
						if (i_155_ < 50) {
							i_154_ = 16711680 + 1280 * i_155_;
						} else if (i_155_ < 100) {
							i_154_ = 0xFFFF00 - 327680 * (i_155_ - 50);
						} else if (i_155_ < 150) {
							i_154_ = 65280 + 5 * (i_155_ - 100);
						}
					}
					if (anIntArray1005[i_148_] == 10) {
						int i_156_ = 150 - anIntArray1007[i_148_];
						if (i_156_ < 50) {
							i_154_ = 16711680 + 5 * i_156_;
						} else if (i_156_ < 100) {
							i_154_ = 16711935 - 327680 * (i_156_ - 50);
						} else if (i_156_ < 150) {
							i_154_ = 255 + 327680 * (i_156_ - 100) - 5 * (i_156_ - 100);
						}
					}
					if (anIntArray1005[i_148_] == 11) {
						int i_157_ = 150 - anIntArray1007[i_148_];
						if (i_157_ < 50) {
							i_154_ = 0xFFFFFF - 327685 * i_157_;
						} else if (i_157_ < 100) {
							i_154_ = 65280 + 327685 * (i_157_ - 50);
						} else if (i_157_ < 150) {
							i_154_ = 0xFFFFFF - 327680 * (i_157_ - 100);
						}
					}
					if (anIntArray1006[i_148_] == 0) {
						fontBold.drawString(string, anInt988, anInt989 + 1, 0);
						fontBold.drawString(string, anInt988, anInt989, i_154_);
					}
					if (anIntArray1006[i_148_] == 1) {
						fontBold.drawCenteredStringWaveY(string, anInt988, anInt989 + 1, anInt1290, 0);
						fontBold.drawCenteredStringWaveY(string, anInt988, anInt989, anInt1290, i_154_);
					}
					if (anIntArray1006[i_148_] == 2) {
						fontBold.drawCeneteredStringWaveXY(string, anInt988, anInt989 + 1, anInt1290, 0);
						fontBold.drawCeneteredStringWaveXY(string, anInt988, anInt989, anInt1290, i_154_);
					}
					if (anIntArray1006[i_148_] == 3) {
						fontBold.drawCenteredStringWaveXYMove(string, anInt988, anInt989 + 1, anInt1290,
								150 - anIntArray1007[i_148_], 0);
						fontBold.drawCenteredStringWaveXYMove(string, anInt988, anInt989, anInt1290,
								150 - anIntArray1007[i_148_], i_154_);
					}
					if (anIntArray1006[i_148_] == 4) {
						int i_158_ = fontBold.getStringWidth(string);
						int i_159_ = (150 - anIntArray1007[i_148_]) * (i_158_ + 100) / 150;
						Rasterizer.setCoordinates(anInt988 - 50, 0, anInt988 + 50, 334);
						fontBold.drawRegularString(string, anInt988 + 50 - i_159_, anInt989 + 1, 0);
						fontBold.drawRegularString(string, anInt988 + 50 - i_159_, anInt989, i_154_);
						Rasterizer.resetCoordinates();
					}
					if (anIntArray1006[i_148_] == 5) {
						int i_160_ = 150 - anIntArray1007[i_148_];
						int i_161_ = 0;
						if (i_160_ < 25) {
							i_161_ = i_160_ - 25;
						} else if (i_160_ > 125) {
							i_161_ = i_160_ - 125;
						}
						Rasterizer.setCoordinates(0, anInt989 - fontBold.characterDefaultHeight - 1, 512, anInt989 + 5);
						fontBold.drawString(string, anInt988, anInt989 + 1 + i_161_, 0);
						fontBold.drawString(string, anInt988, anInt989 + i_161_, i_154_);
						Rasterizer.resetCoordinates();
					}
				} else {
					fontBold.drawString(string, anInt988, anInt989 + 1, 0);
					fontBold.drawString(string, anInt988, anInt989, 0xFFFF00);
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("52196, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method35(boolean bool, long l) {
		try {
			if (l != 0L) {
				for (int i = 0; i < friendsListCount; i++) {
					if (friendsListLongs[i] == l) {
						friendsListCount--;
						drawTabArea = true;
						for (int i_162_ = i; i_162_ < friendsListCount; i_162_++) {
							friendsListNames[i_162_] = friendsListNames[i_162_ + 1];
							friendsListWorlds[i_162_] = friendsListWorlds[i_162_ + 1];
							friendsListLongs[i_162_] = friendsListLongs[i_162_ + 1];
						}
						outBuffer.putOpcode(215);
						outBuffer.putLong(l);
						break;
					}
				}
				if (bool) {
					return;
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("18622, " + bool + ", " + l + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void renderTab() {
		try {
			aProducingGraphicsBuffer1188.createRasterizer();
			Rasterizer3D.lineOffsets = anIntArray1206;
			inventoryBackgroundImage.drawImage(0, 0);
			if (anInt1214 != -1) {
				method105(0, 0, Widget.cache[anInt1214], 0);
			} else if (tabWidgetIndexes[currentTabIndex] != -1) {
				method105(0, 0, Widget.cache[tabWidgetIndexes[currentTabIndex]], 0);
			}
			if (actionMenuOpen && anInt973 == 1) {
				method40();
			}
			aProducingGraphicsBuffer1188.drawGraphics(553, 205, gameGraphics);
			currentSceneBuffer.createRasterizer();
			Rasterizer3D.lineOffsets = anIntArray1207;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("56062, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method37(int i, int i_163_) {
		do {
			try {
				if (i <= 0) {
					opcode = -1;
				}
				if (Client.lowMemory) {
					break;
				}
				if (Rasterizer3D.anIntArray1500[17] >= i_163_) {
					IndexedImage indexedimage = Rasterizer3D.indexedImages[17];
					int i_164_ = indexedimage.width * indexedimage.height - 1;
					int i_165_ = indexedimage.width * anInt970 * 2;
					byte[] bs = indexedimage.pixels;
					byte[] bs_166_ = aByteArray937;
					for (int i_167_ = 0; i_167_ <= i_164_; i_167_++) {
						bs_166_[i_167_] = bs[i_167_ - i_165_ & i_164_];
					}
					indexedimage.pixels = bs_166_;
					aByteArray937 = bs;
					Rasterizer3D.method367(17, -477);
					Client.anInt879++;
					if (Client.anInt879 > 1235) {
						Client.anInt879 = 0;
						outBuffer.putOpcode(226);
						outBuffer.put(0);
						int i_168_ = outBuffer.offset;
						outBuffer.putShort(58722);
						outBuffer.put(240);
						outBuffer.putShort((int) (Math.random() * 65536.0));
						outBuffer.put((int) (Math.random() * 256.0));
						if ((int) (Math.random() * 2.0) == 0) {
							outBuffer.putShort(51825);
						}
						outBuffer.put((int) (Math.random() * 256.0));
						outBuffer.putShort((int) (Math.random() * 65536.0));
						outBuffer.putShort(7130);
						outBuffer.putShort((int) (Math.random() * 65536.0));
						outBuffer.putShort(61657);
						outBuffer.putSizeByte(outBuffer.offset - i_168_);
					}
				}
				if (Rasterizer3D.anIntArray1500[24] >= i_163_) {
					IndexedImage indexedimage = Rasterizer3D.indexedImages[24];
					int i_169_ = indexedimage.width * indexedimage.height - 1;
					int i_170_ = indexedimage.width * anInt970 * 2;
					byte[] bs = indexedimage.pixels;
					byte[] bs_171_ = aByteArray937;
					for (int i_172_ = 0; i_172_ <= i_169_; i_172_++) {
						bs_171_[i_172_] = bs[i_172_ - i_170_ & i_169_];
					}
					indexedimage.pixels = bs_171_;
					aByteArray937 = bs;
					Rasterizer3D.method367(24, -477);
				}
				if (Rasterizer3D.anIntArray1500[34] < i_163_) {
					break;
				}
				IndexedImage indexedimage = Rasterizer3D.indexedImages[34];
				int i_173_ = indexedimage.width * indexedimage.height - 1;
				int i_174_ = indexedimage.width * anInt970 * 2;
				byte[] bs = indexedimage.pixels;
				byte[] bs_175_ = aByteArray937;
				for (int i_176_ = 0; i_176_ <= i_173_; i_176_++) {
					bs_175_[i_176_] = bs[i_176_ - i_174_ & i_173_];
				}
				indexedimage.pixels = bs_175_;
				aByteArray937 = bs;
				Rasterizer3D.method367(34, -477);
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("86342, " + i + ", " + i_163_ + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void method38() {
		try {
			for (int i = -1; i < playerCount; i++) {
				int i_177_;
				if (i == -1) {
					i_177_ = localPlayerIndex;
				} else {
					i_177_ = anIntArray917[i];
				}
				Player player = players[i_177_];
				if (player != null && player.anInt1555 > 0) {
					player.anInt1555--;
					if (player.anInt1555 == 0) {
						player.forcedChat = null;
					}
				}
			}
			for (int i = 0; i < actorCount; i++) {
				int i_178_ = anIntArray862[i];
				Npc npc = actors[i_178_];
				if (npc != null && npc.anInt1555 > 0) {
					npc.anInt1555--;
					if (npc.anInt1555 == 0) {
						npc.forcedChat = null;
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("18071, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method39(byte b) {
		do {
			try {
				int i = anInt1123 * 128 + 64;
				int i_179_ = anInt1124 * 128 + 64;
				int i_180_ = method42(currentSceneIndex, i_179_, true, i) - anInt1125;
				if (anInt883 < i) {
					anInt883 += anInt1126 + (i - anInt883) * anInt1127 / 1000;
					if (anInt883 > i) {
						anInt883 = i;
					}
				}
				if (anInt883 > i) {
					anInt883 -= anInt1126 + (anInt883 - i) * anInt1127 / 1000;
					if (anInt883 < i) {
						anInt883 = i;
					}
				}
				if (anInt884 < i_180_) {
					anInt884 += anInt1126 + (i_180_ - anInt884) * anInt1127 / 1000;
					if (anInt884 > i_180_) {
						anInt884 = i_180_;
					}
				}
				if (anInt884 > i_180_) {
					anInt884 -= anInt1126 + (anInt884 - i_180_) * anInt1127 / 1000;
					if (anInt884 < i_180_) {
						anInt884 = i_180_;
					}
				}
				if (anInt885 < i_179_) {
					anInt885 += anInt1126 + (i_179_ - anInt885) * anInt1127 / 1000;
					if (anInt885 > i_179_) {
						anInt885 = i_179_;
					}
				}
				if (anInt885 > i_179_) {
					anInt885 -= anInt1126 + (anInt885 - i_179_) * anInt1127 / 1000;
					if (anInt885 < i_179_) {
						anInt885 = i_179_;
					}
				}
				i = anInt1020 * 128 + 64;
				i_179_ = anInt1021 * 128 + 64;
				i_180_ = method42(currentSceneIndex, i_179_, true, i) - anInt1022;
				int i_181_ = i - anInt883;
				int i_182_ = i_180_ - anInt884;
				int i_183_ = i_179_ - anInt885;
				int i_184_ = (int) Math.sqrt(i_181_ * i_181_ + i_183_ * i_183_);
				int i_185_ = (int) (Math.atan2(i_182_, i_184_) * 325.949) & 0x7ff;
				int i_186_ = (int) (Math.atan2(i_181_, i_183_) * -325.949) & 0x7ff;
				if (i_185_ < 128) {
					i_185_ = 128;
				}
				if (i_185_ > 383) {
					i_185_ = 383;
				}
				if (anInt886 < i_185_) {
					anInt886 += anInt1023 + (i_185_ - anInt886) * anInt1024 / 1000;
					if (anInt886 > i_185_) {
						anInt886 = i_185_;
					}
				}
				if (anInt886 > i_185_) {
					anInt886 -= anInt1023 + (anInt886 - i_185_) * anInt1024 / 1000;
					if (anInt886 < i_185_) {
						anInt886 = i_185_;
					}
				}
				int i_187_ = i_186_ - anInt887;
				if (i_187_ > 1024) {
					i_187_ -= 2048;
				}
				if (i_187_ < -1024) {
					i_187_ += 2048;
				}
				if (i_187_ > 0) {
					anInt887 += anInt1023 + i_187_ * anInt1024 / 1000;
					anInt887 &= 0x7ff;
				}
				if (i_187_ < 0) {
					anInt887 -= anInt1023 + -i_187_ * anInt1024 / 1000;
					anInt887 &= 0x7ff;
				}
				int i_188_ = i_186_ - anInt887;
				if (i_188_ > 1024) {
					i_188_ -= 2048;
				}
				if (i_188_ < -1024) {
					i_188_ += 2048;
				}
				if ((i_188_ >= 0 || i_187_ <= 0) && (i_188_ <= 0 || i_187_ >= 0)) {
					break;
				}
				anInt887 = i_186_;
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("71397, " + b + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void method40() {
		try {
			int x = anInt974;
			int y = anInt975;
			int width = anInt976;
			int height = anInt977;
			int rgb = 0x5D5447;
			Rasterizer.drawFilledRectangle(x, y, width, height, rgb);
			Rasterizer.drawFilledRectangle(x + 1, y + 1, width - 2, 16, 0);
			Rasterizer.drawUnfilledRectangle(x + 1, y + 18, width - 2, height - 19, 0);
			fontBold.drawRegularString("Choose Option", x + 3, y + 14, rgb);
			int mouseX = mouseEventX;
			int mouseY = mouseEventY;
			if (anInt973 == 0) {
				mouseX -= 4;
				mouseY -= 4;
			}
			if (anInt973 == 1) {
				mouseX -= 553;
				mouseY -= 205;
			}
			if (anInt973 == 2) {
				mouseX -= 17;
				mouseY -= 357;
			}
			for (int i = 0; i < menuActionRow; i++) {
				int yOffset = y + 31 + (menuActionRow - 1 - i) * 15;
				int fontColor = 0xFFFFFF;
				if (mouseX > x && mouseX < x + width && mouseY > yOffset - 13 && mouseY < yOffset + 3) {
					fontColor = 0xFFFF00;
				}
				fontBold.drawShadowedString(menuActionNames[i], x + 3, yOffset, true, fontColor);
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("82996, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method41(byte b, long l) {
		try {
			if (l != 0L) {
				if (friendsListCount >= 100 && anInt1071 != 1) {
					sendMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
				} else if (friendsListCount >= 200) {
					sendMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
				} else {
					String string = TextUtils.formatName(TextUtils.longToName(l));
					for (int i = 0; i < friendsListCount; i++) {
						if (friendsListLongs[i] == l) {
							sendMessage(string + " is already on your friend list", 0, "");
							return;
						}
					}
					if (b != 68) {
						opcode = -1;
					}
					for (int i = 0; i < ignoreListCount; i++) {
						if (ignoreList[i] == l) {
							sendMessage("Please remove " + string + " from your ignore list first", 0, "");
							return;
						}
					}
					if (!string.equals(Client.localPlayer.playerName)) {
						friendsListNames[friendsListCount] = string;
						friendsListLongs[friendsListCount] = l;
						friendsListWorlds[friendsListCount] = 0;
						friendsListCount++;
						drawTabArea = true;
						outBuffer.putOpcode(188);
						outBuffer.putLong(l);
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("15283, " + b + ", " + l + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final int method42(int i, int i_198_, boolean bool, int i_199_) {
		try {
			loggedIn &= bool;
			int i_200_ = i_199_ >> 7;
			int i_201_ = i_198_ >> 7;
			if (i_200_ < 0 || i_201_ < 0 || i_200_ > 103 || i_201_ > 103) {
				return 0;
			}
			int i_202_ = i;
			if (i_202_ < 3 && (currentSceneTileFlags[1][i_200_][i_201_] & 0x2) == 2) {
				i_202_++;
			}
			int i_203_ = i_199_ & 0x7f;
			int i_204_ = i_198_ & 0x7f;
			int i_205_ = anIntArrayArrayArray1239[i_202_][i_200_][i_201_] * (128 - i_203_)
					+ anIntArrayArrayArray1239[i_202_][i_200_ + 1][i_201_] * i_203_ >> 7;
			int i_206_ = anIntArrayArrayArray1239[i_202_][i_200_][i_201_ + 1] * (128 - i_203_)
					+ anIntArrayArrayArray1239[i_202_][i_200_ + 1][i_201_ + 1] * i_203_ >> 7;
			return i_205_ * (128 - i_204_) + i_206_ * i_204_ >> 7;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("48438, " + i + ", " + i_198_ + ", " + bool + ", " + i_199_ + ", "
					+ runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	private static final String method43(int i, int i_207_) {
		try {
			if (i != -33245) {
				Client.anInt871 = -65;
			}
			if (i_207_ < 100000) {
				return String.valueOf(i_207_);
			}
			if (i_207_ < 10000000) {
				return String.valueOf(i_207_ / 1000) + "K";
			}
			return String.valueOf(i_207_ / 1000000) + "M";
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("13837, " + i + ", " + i_207_ + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void logout() {
		try {
			try {
				if (bufferedConnection != null) {
					bufferedConnection.close();
				}
			} catch (Exception exception) {
				/* empty */
			}
			bufferedConnection = null;
			loggedIn = false;
			loginScreenState = 0;
			aString1198 = "";
			aString1199 = "";
			method23(false);
			currentScene.method496(619);
			for (int i = 0; i < 4; i++) {
				currentCollisionMap[i].reset();
			}
			System.gc();
			stopMusic();
			songIndex = -1;
			onDemandRequesterId = -1;
			songFadeCycle = 0;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("91154, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method45(int i) {
		try {
			if (i != 0) {
				opcode = -1;
			}
			aBoolean1056 = true;
			for (int i_208_ = 0; i_208_ < 7; i_208_++) {
				anIntArray1090[i_208_] = -1;
				for (int i_209_ = 0; i_209_ < IdentityKit.count; i_209_++) {
					if (!IdentityKit.cache[i_209_].widgetDisplayed
							&& IdentityKit.cache[i_209_].partId == i_208_ + (aBoolean1072 ? 0 : 7)) {
						anIntArray1090[i_208_] = i_209_;
						break;
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("51214, " + i + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	private final void method46(int i, Buffer buffer, byte b) {
		try {
			if (b != 2) {
				for (int i_210_ = 1; i_210_ > 0; i_210_++) {
					/* empty */
				}
			}
			while (buffer.bitOffset + 21 < i * 8) {
				int i_211_ = buffer.getBits(14);
				if (i_211_ == 16383) {
					break;
				}
				if (actors[i_211_] == null) {
					actors[i_211_] = new Npc();
				}
				Npc npc = actors[i_211_];
				anIntArray862[actorCount++] = i_211_;
				npc.anInt1557 = Client.currentCycle;
				int i_212_ = buffer.getBits(5);
				if (i_212_ > 15) {
					i_212_ -= 32;
				}
				int i_213_ = buffer.getBits(5);
				if (i_213_ > 15) {
					i_213_ -= 32;
				}
				buffer.getBits(1);
				npc.npcDefinition = ActorDefinition.getDefinition(buffer.getBits(12));
				int i_215_ = buffer.getBits(1);
				if (i_215_ == 1) {
					anIntArray919[anInt918++] = i_211_;
				}
				npc.boundaryDimension = npc.npcDefinition.boundaryDimension;
				npc.anInt1524 = npc.npcDefinition.degreesToTurn;
				npc.walkAnimationId = npc.npcDefinition.walkAnimationId;
				npc.turnAroundAnimationId = npc.npcDefinition.turnAroundAnimationId;
				npc.turnRightAnimationId = npc.npcDefinition.turnRightAnimationId;
				npc.turnLeftAnimationId = npc.npcDefinition.turnLeftAnimationId;
				npc.standAnimationId = npc.npcDefinition.standAnimationId;
				npc.setPosition(Client.localPlayer.pathX[0] + i_213_, Client.localPlayer.pathY[0] + i_212_);
			}
			buffer.finishBitAccess();
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("75258, " + i + ", " + buffer + ", " + b + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	@Override
	public final void doLogic() {
		if (!alreadyLoaded && !loadingError && !genericLoadingError) {
			Client.currentCycle++;
			if (!loggedIn) {
				processLoginScreen();
			} else {
				processGame();
			}
			processOnDemand();
		}
	}

	public final void method47(int i, boolean bool) {
		try {
			if (Client.localPlayer.xWithBoundary >> 7 == destinationX
					&& Client.localPlayer.yWithBoundary >> 7 == destinationY) {
				destinationX = 0;
			}
			int i_216_ = playerCount;
			if (i != 0) {
				for (int i_217_ = 1; i_217_ > 0; i_217_++) {
					/* empty */
				}
			}
			if (bool) {
				i_216_ = 1;
			}
			int i_218_ = 0;
			for (/**/; i_218_ < i_216_; i_218_++) {
				Player player;
				int i_219_;
				if (bool) {
					player = Client.localPlayer;
					i_219_ = localPlayerIndex << 14;
				} else {
					player = players[anIntArray917[i_218_]];
					i_219_ = anIntArray917[i_218_] << 14;
				}
				if (player != null && player.isVisibile()) {
					player.aBoolean1719 = false;
					if ((Client.lowMemory && playerCount > 50 || playerCount > 200) && !bool
							&& player.anInt1537 == player.standAnimationId) {
						player.aBoolean1719 = true;
					}
					int i_220_ = player.xWithBoundary >> 7;
					int i_221_ = player.yWithBoundary >> 7;
					if (i_220_ >= 0 && i_220_ < 104 && i_221_ >= 0 && i_221_ < 104) {
						if (player.aModel1734 != null && Client.currentCycle >= player.anInt1727
								&& Client.currentCycle < player.anInt1728) {
							player.aBoolean1719 = false;
							player.anInt1729 = method42(currentSceneIndex, player.yWithBoundary, true,
									player.xWithBoundary);
							currentScene.method508(60, currentSceneIndex, player.yWithBoundary, player,
									player.anInt1572, player.anInt1742, player.xWithBoundary, player.anInt1729,
									player.anInt1739, player.anInt1741, i_219_, player.anInt1740, (byte) 35);
						} else {
							if ((player.xWithBoundary & 0x7f) == 64 && (player.yWithBoundary & 0x7f) == 64) {
								if (anIntArrayArray954[i_220_][i_221_] == anInt1290) {
									continue;
								}
								anIntArrayArray954[i_220_][i_221_] = anInt1290;
							}
							player.anInt1729 = method42(currentSceneIndex, player.yWithBoundary, true,
									player.xWithBoundary);
							currentScene
									.method507(currentSceneIndex, player.anInt1572, (byte) 6, player.anInt1729, i_219_,
											player.yWithBoundary, 60, player.xWithBoundary, player, player.aBoolean1561);
						}
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("79491, " + i + ", " + bool + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final boolean method48(Widget widget) {
		try {
			int type = widget.contentType;
			if (friendListStatus == 2) {
				if (type == 201) {
					aBoolean1248 = true;
					inputType = 0;
					aBoolean1281 = true;
					chatMessage = "";
					anInt1089 = 1;
					inputCustomMessage = "Enter name of friend to add to list";
				}
				if (type == 202) {
					aBoolean1248 = true;
					inputType = 0;
					aBoolean1281 = true;
					chatMessage = "";
					anInt1089 = 2;
					inputCustomMessage = "Enter name of friend to delete from list";
				}
			}
			if (type == 205) {
				anInt1036 = 250;
				return true;
			}
			if (type == 501) {
				aBoolean1248 = true;
				inputType = 0;
				aBoolean1281 = true;
				chatMessage = "";
				anInt1089 = 4;
				inputCustomMessage = "Enter name of player to add to list";
			}
			if (type == 502) {
				aBoolean1248 = true;
				inputType = 0;
				aBoolean1281 = true;
				chatMessage = "";
				anInt1089 = 5;
				inputCustomMessage = "Enter name of player to delete from list";
			}
			if (type >= 300 && type <= 313) {
				int i_223_ = (type - 300) / 2;
				int i_224_ = type & 0x1;
				int i_225_ = anIntArray1090[i_223_];
				if (i_225_ != -1) {
					do {
						if (i_224_ == 0 && --i_225_ < 0) {
							i_225_ = IdentityKit.count - 1;
						}
						if (i_224_ == 1 && ++i_225_ >= IdentityKit.count) {
							i_225_ = 0;
						}
					} while (IdentityKit.cache[i_225_].widgetDisplayed
							|| IdentityKit.cache[i_225_].partId != i_223_ + (aBoolean1072 ? 0 : 7));
					anIntArray1090[i_223_] = i_225_;
					aBoolean1056 = true;
				}
			}
			if (type >= 314 && type <= 323) {
				int i_226_ = (type - 314) / 2;
				int i_227_ = type & 0x1;
				int i_228_ = anIntArray1015[i_226_];
				if (i_227_ == 0 && --i_228_ < 0) {
					i_228_ = Client.anIntArrayArray1028[i_226_].length - 1;
				}
				if (i_227_ == 1 && ++i_228_ >= Client.anIntArrayArray1028[i_226_].length) {
					i_228_ = 0;
				}
				anIntArray1015[i_226_] = i_228_;
				aBoolean1056 = true;
			}
			if (type == 324 && !aBoolean1072) {
				aBoolean1072 = true;
				method45(0);
			}
			if (type == 325 && aBoolean1072) {
				aBoolean1072 = false;
				method45(0);
			}
			if (type == 326) {
				outBuffer.putOpcode(101);
				outBuffer.put(aBoolean1072 ? 0 : 1);
				for (int i_229_ = 0; i_229_ < 7; i_229_++) {
					outBuffer.put(anIntArray1090[i_229_]);
				}
				for (int i_230_ = 0; i_230_ < 5; i_230_++) {
					outBuffer.put(anIntArray1015[i_230_]);
				}
				return true;
			}
			if (type == 613) {
				aBoolean1183 = !aBoolean1183;
			}
			if (type >= 601 && type <= 612) {
				closeWidgets();
				if (reportWidgetName.length() > 0) {
					outBuffer.putOpcode(218);
					outBuffer.putLong(TextUtils.nameToLong(reportWidgetName));
					outBuffer.put(type - 601);
					outBuffer.put(aBoolean1183 ? 1 : 0);
				}
			}
			return false;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("3833, " + widget + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	private final void method49(Buffer buffer) {
		try {
			for (int i = 0; i < anInt918; i++) {
				int playerIndex = anIntArray919[i];
				Player player = players[playerIndex];
				int updateMask = buffer.getUnsignedByte();
				if ((updateMask & 0x40) != 0) {
					updateMask += buffer.getUnsignedByte() << 8;
				}
				updatePlayerMask(updateMask, playerIndex, buffer, player);
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("72508, " + buffer + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void renderCurrentScene(int i, int i_234_, int i_235_, int i_236_, int i_237_, int i_238_) {
		do {
			try {
				int i_239_ = currentScene.method522(i_238_, i_236_, i);
				if (i_234_ < 0) {
					if (i_239_ != 0) {
						int i_240_ = currentScene.method526(i_238_, i_236_, i, i_239_);
						int i_241_ = i_240_ >> 6 & 0x3;
						int i_242_ = i_240_ & 0x1f;
						int i_243_ = i_235_;
						if (i_239_ > 0) {
							i_243_ = i_237_;
						}
						int[] is = minimapImage.pixels;
						int i_244_ = 24624 + i_236_ * 4 + (103 - i) * 512 * 4;
						int i_245_ = i_239_ >> 14 & 0x7fff;
						GameObjectDefinition gameobjectdefinition = GameObjectDefinition.getDefinition(i_245_);
						if (gameobjectdefinition.anInt263 != -1) {
							IndexedImage indexedimage = anIndexedImageArray1085[gameobjectdefinition.anInt263];
							if (indexedimage != null) {
								int i_246_ = (gameobjectdefinition.width * 4 - indexedimage.width) / 2;
								int i_247_ = (gameobjectdefinition.height * 4 - indexedimage.height) / 2;
								indexedimage.drawImage(48 + i_236_ * 4 + i_246_, 48
										+ (104 - i - gameobjectdefinition.height) * 4 + i_247_);
							}
						} else {
							if (i_242_ == 0 || i_242_ == 2) {
								if (i_241_ == 0) {
									is[i_244_] = i_243_;
									is[i_244_ + 512] = i_243_;
									is[i_244_ + 1024] = i_243_;
									is[i_244_ + 1536] = i_243_;
								} else if (i_241_ == 1) {
									is[i_244_] = i_243_;
									is[i_244_ + 1] = i_243_;
									is[i_244_ + 2] = i_243_;
									is[i_244_ + 3] = i_243_;
								} else if (i_241_ == 2) {
									is[i_244_ + 3] = i_243_;
									is[i_244_ + 3 + 512] = i_243_;
									is[i_244_ + 3 + 1024] = i_243_;
									is[i_244_ + 3 + 1536] = i_243_;
								} else if (i_241_ == 3) {
									is[i_244_ + 1536] = i_243_;
									is[i_244_ + 1536 + 1] = i_243_;
									is[i_244_ + 1536 + 2] = i_243_;
									is[i_244_ + 1536 + 3] = i_243_;
								}
							}
							if (i_242_ == 3) {
								if (i_241_ == 0) {
									is[i_244_] = i_243_;
								} else if (i_241_ == 1) {
									is[i_244_ + 3] = i_243_;
								} else if (i_241_ == 2) {
									is[i_244_ + 3 + 1536] = i_243_;
								} else if (i_241_ == 3) {
									is[i_244_ + 1536] = i_243_;
								}
							}
							if (i_242_ == 2) {
								if (i_241_ == 3) {
									is[i_244_] = i_243_;
									is[i_244_ + 512] = i_243_;
									is[i_244_ + 1024] = i_243_;
									is[i_244_ + 1536] = i_243_;
								} else if (i_241_ == 0) {
									is[i_244_] = i_243_;
									is[i_244_ + 1] = i_243_;
									is[i_244_ + 2] = i_243_;
									is[i_244_ + 3] = i_243_;
								} else if (i_241_ == 1) {
									is[i_244_ + 3] = i_243_;
									is[i_244_ + 3 + 512] = i_243_;
									is[i_244_ + 3 + 1024] = i_243_;
									is[i_244_ + 3 + 1536] = i_243_;
								} else if (i_241_ == 2) {
									is[i_244_ + 1536] = i_243_;
									is[i_244_ + 1536 + 1] = i_243_;
									is[i_244_ + 1536 + 2] = i_243_;
									is[i_244_ + 1536 + 3] = i_243_;
								}
							}
						}
					}
					i_239_ = currentScene.method524(i_238_, i_236_, i);
					if (i_239_ != 0) {
						int i_248_ = currentScene.method526(i_238_, i_236_, i, i_239_);
						int i_249_ = i_248_ >> 6 & 0x3;
						int i_250_ = i_248_ & 0x1f;
						int i_251_ = i_239_ >> 14 & 0x7fff;
						GameObjectDefinition gameobjectdefinition = GameObjectDefinition.getDefinition(i_251_);
						if (gameobjectdefinition.anInt263 != -1) {
							IndexedImage indexedimage = anIndexedImageArray1085[gameobjectdefinition.anInt263];
							if (indexedimage != null) {
								int i_252_ = (gameobjectdefinition.width * 4 - indexedimage.width) / 2;
								int i_253_ = (gameobjectdefinition.height * 4 - indexedimage.height) / 2;
								indexedimage.drawImage(48 + i_236_ * 4 + i_252_, 48
										+ (104 - i - gameobjectdefinition.height) * 4 + i_253_);
							}
						} else if (i_250_ == 9) {
							int i_254_ = 15658734;
							if (i_239_ > 0) {
								i_254_ = 15597568;
							}
							int[] is = minimapImage.pixels;
							int i_255_ = 24624 + i_236_ * 4 + (103 - i) * 512 * 4;
							if (i_249_ == 0 || i_249_ == 2) {
								is[i_255_ + 1536] = i_254_;
								is[i_255_ + 1024 + 1] = i_254_;
								is[i_255_ + 512 + 2] = i_254_;
								is[i_255_ + 3] = i_254_;
							} else {
								is[i_255_] = i_254_;
								is[i_255_ + 512 + 1] = i_254_;
								is[i_255_ + 1024 + 2] = i_254_;
								is[i_255_ + 1536 + 3] = i_254_;
							}
						}
					}
					i_239_ = currentScene.getFloorHash(i_238_, i_236_, i);
					if (i_239_ == 0) {
						break;
					}
					int i_256_ = i_239_ >> 14 & 0x7fff;
					GameObjectDefinition gameobjectdefinition = GameObjectDefinition.getDefinition(i_256_);
					if (gameobjectdefinition.anInt263 == -1) {
						break;
					}
					IndexedImage indexedimage = anIndexedImageArray1085[gameobjectdefinition.anInt263];
					if (indexedimage == null) {
						break;
					}
					int i_257_ = (gameobjectdefinition.width * 4 - indexedimage.width) / 2;
					int i_258_ = (gameobjectdefinition.height * 4 - indexedimage.height) / 2;
					indexedimage.drawImage(48 + i_236_ * 4 + i_257_, 48 + (104 - i - gameobjectdefinition.height) * 4
							+ i_258_);
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("19786, " + i + ", " + i_234_ + ", " + i_235_ + ", " + i_236_ + ", " + i_237_
						+ ", " + i_238_ + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void prepareTitle() {
		do {
			try {
				titleboxImage = new IndexedImage(anArchive1078, "titlebox", 0);
				titleboxButtonImage = new IndexedImage(anArchive1078, "titlebutton", 0);
				titleFlameEmblem = new IndexedImage[12];

				int icon = 0;
				try {
					icon = Integer.parseInt(getParameter("fl_icon"));
				} catch (Exception exception) {
					/* empty */
				}
				if (icon == 0) {
					for (int i = 0; i < 12; i++) {
						titleFlameEmblem[i] = new IndexedImage(anArchive1078, "runes", i);
					}
				} else {
					for (int i = 0; i < 12; i++) {
						titleFlameEmblem[i] = new IndexedImage(anArchive1078, "runes", 12 + (i & 0x3));
					}
				}

				anImageRGB1226 = new ImageRGB(128, 265);
				anImageRGB1227 = new ImageRGB(128, 265);

				// Copies the background behind the flames, to the flame's
				// pixels before the actual flames are rendered.
				System.arraycopy(flameLeftBackground.pixels, 0, anImageRGB1226.pixels, 0, (128 * 265));
				System.arraycopy(flameRightBackground.pixels, 0, anImageRGB1227.pixels, 0, (128 * 265));

				anIntArray876 = new int[256];
				for (int i = 0; i < 64; i++) {
					anIntArray876[i] = i * 0x040000;
				}
				for (int i = 0; i < 64; i++) {
					anIntArray876[i + 64] = 0xFF0000 + (0x000400 * i);
				}
				for (int i = 0; i < 64; i++) {
					anIntArray876[i + 128] = 0xFFFF00 + (0x04 * i);
				}
				for (int i = 0; i < 64; i++) {
					anIntArray876[i + 192] = 0xFFFFFF;
				}

				anIntArray877 = new int[256];
				for (int i = 0; i < 64; i++) {
					anIntArray877[i] = i * 0x000400;
				}
				for (int i = 0; i < 64; i++) {
					anIntArray877[i + 64] = 0x00FF00 + (0x04 * i);
				}
				for (int i = 0; i < 64; i++) {
					anIntArray877[i + 128] = 0x00FFFF + (0x040000 * i);
				}
				for (int i = 0; i < 64; i++) {
					anIntArray877[i + 192] = 0xFFFFFF;
				}

				anIntArray878 = new int[256];
				for (int i = 0; i < 64; i++) {
					anIntArray878[i] = i * 4;
				}
				for (int i = 0; i < 64; i++) {
					anIntArray878[i + 64] = 0xFF + (0x040000 * i);
				}
				for (int i = 0; i < 64; i++) {
					anIntArray878[i + 128] = 0xFF00FF + (0x0400 * i);
				}
				for (int i = 0; i < 64; i++) {
					anIntArray878[i + 192] = 0xFFFFFF;
				}

				anIntArray875 = new int[256];
				anIntArray1215 = new int[32768];
				anIntArray1216 = new int[32768];
				method106(null, -135);
				anIntArray853 = new int[32768];
				anIntArray854 = new int[32768];
				drawLoadingText(10, "Connecting to fileserver");
				if (aBoolean856) {
					break;
				}
				aBoolean905 = true;
				aBoolean856 = true;
				startRunnable(this, 2);
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("57668, " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public static final void setHighMemory() {
		Scene.lowMemory = false;
		Rasterizer3D.lowMemory = false;
		Client.lowMemory = false;
		Region.lowMemory = false;
		GameObjectDefinition.lowMemory = false;
	}

	public static final void main(String[] strings) {
		try {
			System.out.println("RS2 user client - release #" + 317);
			Client.nodeIndex = 0;
			Client.portOffset = 0;
			// setLowMemory((byte) 77);
			Client.setHighMemory();
			Client.membersWorld = true;
			SignLink.storeIndex = 32;
			SignLink.initialize(InetAddress.getLocalHost());
			Client client = new Client();
			client.initializeApplication(765, 503);
		} catch (Exception exception) {
			/* empty */
		}
	}

	public final void method53() {
		try {
			if (Client.lowMemory && anInt1048 == 2 && Region.onBuildTimePlane != currentSceneIndex) {
				currentSceneBuffer.createRasterizer();
				fontNormal.drawString("Loading - please wait.", 257, 151, 0);
				fontNormal.drawString("Loading - please wait.", 256, 150, 0xFFFFFF);
				currentSceneBuffer.drawGraphics(4, 4, gameGraphics);
				anInt1048 = 1;
				aLong849 = System.currentTimeMillis();
			}
			if (anInt1048 == 1) {
				int i_276_ = method54();
				if (i_276_ != 0 && System.currentTimeMillis() - aLong849 > 360000L) {
					SignLink.reportError(aString1198 + " glcfb " + aLong1240 + "," + i_276_ + "," + Client.lowMemory
							+ "," + stores[0] + "," + onDemandRequester.immediateRequestCount() + ","
							+ currentSceneIndex + "," + anInt1094 + "," + anInt1095);
					aLong849 = System.currentTimeMillis();
				}
			}
			if (anInt1048 != 2 || currentSceneIndex == lastSceneIndex) {
				return;
			}
			lastSceneIndex = currentSceneIndex;
			renderViewport(currentSceneIndex);
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("51136, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final int method54() {
		try {
			for (int i = 0; i < aByteArrayArray1208.length; i++) {
				if (aByteArrayArray1208[i] == null && anIntArray1260[i] != -1) {
					return -1;
				}
				if (aByteArrayArray1272[i] == null && anIntArray1261[i] != -1) {
					return -2;
				}
			}
			boolean bool = true;
			for (int i = 0; i < aByteArrayArray1208.length; i++) {
				byte[] bs = aByteArrayArray1272[i];
				if (bs != null) {
					int i_277_ = (anIntArray1259[i] >> 8) * 64 - regionAbsoluteBaseX;
					int i_278_ = (anIntArray1259[i] & 0xff) * 64 - regionAbsoluteBaseY;
					if (aBoolean1184) {
						i_277_ = 10;
						i_278_ = 10;
					}
					bool &= Region.method471(i_277_, bs, i_278_, 6);
				}
			}
			if (!bool) {
				return -3;
			}
			if (aBoolean1105) {
				return -4;
			}
			anInt1048 = 2;
			Region.onBuildTimePlane = currentSceneIndex;
			method22();
			outBuffer.putOpcode(121);
			return 0;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("50361, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method55(int i) {
		try {
			while (i >= 0) {
				startup();
			}
			for (Projectile projectile = (Projectile) projectileList.getBack(); projectile != null; projectile = (Projectile) projectileList
					.getPrevious()) {
				if (projectile.sceneId != currentSceneIndex || Client.currentCycle > projectile.endCycle) {
					projectile.remove();
				} else if (Client.currentCycle >= projectile.delay) {
					if (projectile.targetedEntityIndex > 0) {
						Npc npc = actors[projectile.targetedEntityIndex - 1];
						if (npc != null && npc.xWithBoundary >= 0 && npc.xWithBoundary < 13312
								&& npc.yWithBoundary >= 0 && npc.yWithBoundary < 13312) {
							projectile.trackTarget(Client.currentCycle, npc.yWithBoundary,
									method42(projectile.sceneId, npc.yWithBoundary, true, npc.xWithBoundary)
											- projectile.endHeight, npc.xWithBoundary);
						}
					}
					if (projectile.targetedEntityIndex < 0) {
						int i_279_ = -projectile.targetedEntityIndex - 1;
						Player player;
						if (i_279_ == anInt909) {
							player = Client.localPlayer;
						} else {
							player = players[i_279_];
						}
						if (player != null && player.xWithBoundary >= 0 && player.xWithBoundary < 13312
								&& player.yWithBoundary >= 0 && player.yWithBoundary < 13312) {
							projectile.trackTarget(Client.currentCycle, player.yWithBoundary,
									method42(projectile.sceneId, player.yWithBoundary, true, player.xWithBoundary)
											- projectile.endHeight, player.xWithBoundary);
						}
					}
					projectile.move(anInt970);
					currentScene.method507(currentSceneIndex, projectile.modelRotationY, (byte) 6,
							(int) projectile.currentHeight, -1, (int) projectile.currentY, 60,
							(int) projectile.currentX, projectile, false);
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("65179, " + i + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	@Override
	public final AppletContext getAppletContext() {
		if (SignLink.applet != null) {
			return SignLink.applet.getAppletContext();
		}
		return super.getAppletContext();
	}

	public final void prepareTitleBackground() {
		try {
			byte[] data = anArchive1078.getFile("title.dat");
			ImageRGB image = new ImageRGB(data, this);
			flameLeftBackground.createRasterizer();
			image.drawInverse(0, 0);
			flameRightBackground.createRasterizer();
			image.drawInverse(-637, 0);
			aProducingGraphicsBuffer1132.createRasterizer();
			image.drawInverse(-128, 0);
			aProducingGraphicsBuffer1133.createRasterizer();
			image.drawInverse(-202, -371);
			aProducingGraphicsBuffer1134.createRasterizer();
			image.drawInverse(-202, -171);
			aProducingGraphicsBuffer1137.createRasterizer();
			image.drawInverse(0, -265);
			aProducingGraphicsBuffer1138.createRasterizer();
			image.drawInverse(-562, -265);
			aProducingGraphicsBuffer1139.createRasterizer();
			image.drawInverse(-128, -171);
			aProducingGraphicsBuffer1140.createRasterizer();
			image.drawInverse(-562, -171);
			int[] is = new int[image.width];
			for (int i_280_ = 0; i_280_ < image.height; i_280_++) {
				for (int i_281_ = 0; i_281_ < image.width; i_281_++) {
					is[i_281_] = image.pixels[image.width - i_281_ - 1 + image.width * i_280_];
				}
				for (int i_282_ = 0; i_282_ < image.width; i_282_++) {
					image.pixels[i_282_ + image.width * i_280_] = is[i_282_];
				}
			}
			flameLeftBackground.createRasterizer();
			image.drawInverse(382, 0);
			flameRightBackground.createRasterizer();
			image.drawInverse(-255, 0);
			aProducingGraphicsBuffer1132.createRasterizer();
			image.drawInverse(254, 0);
			aProducingGraphicsBuffer1133.createRasterizer();
			image.drawInverse(180, -371);
			aProducingGraphicsBuffer1134.createRasterizer();
			image.drawInverse(180, -171);
			aProducingGraphicsBuffer1137.createRasterizer();
			image.drawInverse(382, -265);
			aProducingGraphicsBuffer1138.createRasterizer();
			image.drawInverse(-180, -265);
			aProducingGraphicsBuffer1139.createRasterizer();
			image.drawInverse(254, -171);
			aProducingGraphicsBuffer1140.createRasterizer();
			image.drawInverse(-180, -171);
			image = new ImageRGB(anArchive1078, "logo", 0);
			aProducingGraphicsBuffer1132.createRasterizer();
			image.drawImage(382 - (image.width / 2) - 128, 18);
			System.gc();
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("96255, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void processOnDemand() {
		while (true) {
			OnDemandNode onDemandNode = onDemandRequester.next();
			if (onDemandNode == null) {
				break;
			}
			if (onDemandNode.type == 0) {
				Model.loadModelHeader(onDemandNode.buffer, onDemandNode.id);
				if ((onDemandRequester.modelIndex(onDemandNode.id) & 0x62) != 0) {
					drawTabArea = true;
					if (chatboxWidgetIndex != -1) {
						aBoolean1248 = true;
					}
				}
			}
			if (onDemandNode.type == 1 && onDemandNode.buffer != null) {
				Animation.method149(onDemandNode.buffer, false);
			}
			if (onDemandNode.type == 2 && onDemandNode.id == onDemandRequesterId && onDemandNode.buffer != null) {
				method21(midiFade, 0, onDemandNode.buffer);
			}
			if (onDemandNode.type == 3 && anInt1048 == 1) {
				for (int i = 0; i < aByteArrayArray1208.length; i++) {
					if (anIntArray1260[i] == onDemandNode.id) {
						aByteArrayArray1208[i] = onDemandNode.buffer;
						if (onDemandNode.buffer == null) {
							anIntArray1260[i] = -1;
						}
						break;
					}
					if (anIntArray1261[i] == onDemandNode.id) {
						aByteArrayArray1272[i] = onDemandNode.buffer;
						if (onDemandNode.buffer == null) {
							anIntArray1261[i] = -1;
						}
						break;
					}
				}
			}
			if (onDemandNode.type == 93 && onDemandRequester.landIndexExists(onDemandNode.id)) {
				Region.passiveRequestGameObjectModels(new Buffer(onDemandNode.buffer), onDemandRequester);
			}
		}
	}

	public final void renderFlames() {
		do {
			try {
				int i_285_ = 256;
				for (int i_286_ = 10; i_286_ < 117; i_286_++) {
					int i_287_ = (int) (Math.random() * 100.0);
					if (i_287_ < 50) {
						anIntArray853[i_286_ + (i_285_ - 2 << 7)] = 255;
					}
				}
				for (int i_288_ = 0; i_288_ < 100; i_288_++) {
					int i_289_ = (int) (Math.random() * 124.0) + 2;
					int i_290_ = (int) (Math.random() * 128.0) + 128;
					int i_291_ = i_289_ + (i_290_ << 7);
					anIntArray853[i_291_] = 192;
				}
				for (int i_292_ = 1; i_292_ < i_285_ - 1; i_292_++) {
					for (int i_293_ = 1; i_293_ < 127; i_293_++) {
						int i_294_ = i_293_ + (i_292_ << 7);
						anIntArray854[i_294_] = (anIntArray853[i_294_ - 1] + anIntArray853[i_294_ + 1]
								+ anIntArray853[i_294_ - 128] + anIntArray853[i_294_ + 128]) / 4;
					}
				}
				anInt1300 += 128;
				if (anInt1300 > anIntArray1215.length) {
					anInt1300 -= anIntArray1215.length;
					int i_295_ = (int) (Math.random() * 12.0);
					method106(titleFlameEmblem[i_295_], -135);
				}
				for (int i_296_ = 1; i_296_ < i_285_ - 1; i_296_++) {
					for (int i_297_ = 1; i_297_ < 127; i_297_++) {
						int i_298_ = i_297_ + (i_296_ << 7);
						int i_299_ = anIntArray854[i_298_ + 128]
								- anIntArray1215[i_298_ + anInt1300 & anIntArray1215.length - 1] / 5;
						if (i_299_ < 0) {
							i_299_ = 0;
						}
						anIntArray853[i_298_] = i_299_;
					}
				}
				for (int i_300_ = 0; i_300_ < i_285_ - 1; i_300_++) {
					anIntArray994[i_300_] = anIntArray994[i_300_ + 1];
				}
				anIntArray994[i_285_ - 1] = (int) (Math.sin(Client.currentCycle / 14.0) * 16.0
						+ Math.sin(Client.currentCycle / 15.0) * 14.0 + Math.sin(Client.currentCycle / 16.0) * 12.0);
				if (anInt1065 > 0) {
					anInt1065 -= 4;
				}
				if (anInt1066 > 0) {
					anInt1066 -= 4;
				}
				if (anInt1065 != 0 || anInt1066 != 0) {
					break;
				}
				int i_301_ = (int) (Math.random() * 2000.0);
				if (i_301_ == 0) {
					anInt1065 = 1024;
				}
				if (i_301_ != 1) {
					break;
				}
				anInt1066 = 1024;
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("52615, " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void method60(int i) {
		try {
			Widget widget = Widget.cache[i];
			for (int i_302_ = 0; i_302_ < widget.children.length; i_302_++) {
				if (widget.children[i_302_] == -1) {
					break;
				}
				Widget widget_303_ = Widget.cache[widget.children[i_302_]];
				if (widget_303_.type == 1) {
					method60(widget_303_.index);
				}
				widget_303_.animationFrame = 0;
				widget_303_.animationDuration = 0;
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("61586, " + i + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method61() {
		do {
			try {
				if (hintIconType == 2) {
					method128((hintIconX - regionAbsoluteBaseX << 7) + anInt962, hintIconOffset * 2, anInt900,
							(hintIconY - regionAbsoluteBaseY << 7) + anInt963);
					if (anInt988 <= -1 || Client.currentCycle % 20 >= 10) {
						break;
					}
					anImageRGBArray1120[2].drawImage(anInt988 - 12, anInt989 - 28);
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("10525, " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void processGame() {
		do {
			try {
				if (systemUpdateTime > 1) {
					systemUpdateTime--;
				}
				if (anInt1036 > 0) {
					anInt1036--;
				}
				for (int i_304_ = 0; i_304_ < 5; i_304_++) {
					if (!method145(true)) {
						break;
					}
				}
				if (loggedIn) {
					synchronized (mouseCapturer.objectLock) {
						if (Client.flagged) {
							if (clickType != 0 || mouseCapturer.coord >= 40) {
								outBuffer.putOpcode(45);
								outBuffer.put(0);
								int offset = outBuffer.offset;
								int i_306_ = 0;
								for (int i_307_ = 0; i_307_ < mouseCapturer.coord; i_307_++) {
									if (offset - outBuffer.offset >= 240) {
										break;
									}
									i_306_++;
									int i_308_ = mouseCapturer.coordsY[i_307_];
									if (i_308_ < 0) {
										i_308_ = 0;
									} else if (i_308_ > 502) {
										i_308_ = 502;
									}
									int i_309_ = mouseCapturer.coordsX[i_307_];
									if (i_309_ < 0) {
										i_309_ = 0;
									} else if (i_309_ > 764) {
										i_309_ = 764;
									}
									int i_310_ = i_308_ * 765 + i_309_;
									if (mouseCapturer.coordsY[i_307_] == -1 && mouseCapturer.coordsX[i_307_] == -1) {
										i_309_ = -1;
										i_308_ = -1;
										i_310_ = 524287;
									}
									if (i_309_ == anInt1262 && i_308_ == anInt1263) {
										if (anInt1047 < 2047) {
											anInt1047++;
										}
									} else {
										int i_311_ = i_309_ - anInt1262;
										anInt1262 = i_309_;
										int i_312_ = i_308_ - anInt1263;
										anInt1263 = i_308_;
										if (anInt1047 < 8 && i_311_ >= -32 && i_311_ <= 31 && i_312_ >= -32
												&& i_312_ <= 31) {
											i_311_ += 32;
											i_312_ += 32;
											outBuffer.putShort((anInt1047 << 12) + (i_311_ << 6) + i_312_);
											anInt1047 = 0;
										} else if (anInt1047 < 8) {
											outBuffer.put24BitInt(8388608 + (anInt1047 << 19) + i_310_);
											anInt1047 = 0;
										} else {
											outBuffer.putInt(-1073741824 + (anInt1047 << 19) + i_310_);
											anInt1047 = 0;
										}
									}
								}
								outBuffer.putSizeByte(outBuffer.offset - offset);
								if (i_306_ >= mouseCapturer.coord) {
									mouseCapturer.coord = 0;
								} else {
									mouseCapturer.coord -= i_306_;
									for (int i_313_ = 0; i_313_ < mouseCapturer.coord; i_313_++) {
										mouseCapturer.coordsX[i_313_] = mouseCapturer.coordsX[i_313_ + i_306_];
										mouseCapturer.coordsY[i_313_] = mouseCapturer.coordsY[i_313_ + i_306_];
									}
								}
							}
						} else {
							mouseCapturer.coord = 0;
						}
					}
					if (clickType != 0) {
						long l = (clickTime - aLong1245) / 50L;
						if (l > 4095L) {
							l = 4095L;
						}
						aLong1245 = clickTime;
						int i_314_ = clickY;
						if (i_314_ < 0) {
							i_314_ = 0;
						} else if (i_314_ > 502) {
							i_314_ = 502;
						}
						int i_315_ = clickX;
						if (i_315_ < 0) {
							i_315_ = 0;
						} else if (i_315_ > 764) {
							i_315_ = 764;
						}
						int i_316_ = i_314_ * 765 + i_315_;
						int i_317_ = 0;
						if (clickType == 2) {
							i_317_ = 1;
						}
						int i_318_ = (int) l;
						outBuffer.putOpcode(241);
						outBuffer.putInt((i_318_ << 20) + (i_317_ << 19) + i_316_);
					}
					if (anInt1041 > 0) {
						anInt1041--;
					}
					if (keyStatus[1] == 1 || keyStatus[2] == 1 || keyStatus[3] == 1 || keyStatus[4] == 1) {
						aBoolean1042 = true;
					}
					if (aBoolean1042 && anInt1041 <= 0) {
						anInt1041 = 20;
						aBoolean1042 = false;
						outBuffer.putOpcode(86);
						outBuffer.putShort(anInt1209);
						outBuffer.putShortA(anInt1210);
					}
					if (awtFocus == true && aBoolean979 == false) {
						aBoolean979 = true;
						outBuffer.putOpcode(3);
						outBuffer.put(1);
					}
					if (awtFocus == false && aBoolean979 == true) {
						aBoolean979 = false;
						outBuffer.putOpcode(3);
						outBuffer.put(0);
					}
					method53();
					method115();
					method90();
					anInt1034++;
					if (anInt1034 > 750) {
						method68(-670);
					}
					method114();
					method95();
					method38();
					anInt970++;
					if (anInt942 != 0) {
						anInt941 += 20;
						if (anInt941 >= 400) {
							anInt942 = 0;
						}
					}
					if (anInt1271 != 0) {
						anInt1268++;
						if (anInt1268 >= 15) {
							if (anInt1271 == 2) {
								drawTabArea = true;
							}
							if (anInt1271 == 3) {
								aBoolean1248 = true;
							}
							anInt1271 = 0;
						}
					}
					if (anInt1111 != 0) {
						anInt1014++;
						if (mouseEventX > anInt1112 + 5 || mouseEventX < anInt1112 - 5 || mouseEventY > anInt1113 + 5
								|| mouseEventY < anInt1113 - 5) {
							aBoolean1267 = true;
						}
						if (mouseButtonPressed == 0) {
							if (anInt1111 == 2) {
								drawTabArea = true;
							}
							if (anInt1111 == 3) {
								aBoolean1248 = true;
							}
							anInt1111 = 0;
							if (aBoolean1267 && anInt1014 >= 5) {
								anInt1092 = -1;
								processClickingAreas();
								if (anInt1092 == anInt1109 && anInt1091 != anInt1110) {
									Widget widget = Widget.cache[anInt1109];
									int i_319_ = 0;
									if (anInt938 == 1 && widget.contentType == 206) {
										i_319_ = 1;
									}
									if (widget.items[anInt1091] <= 0) {
										i_319_ = 0;
									}
									if (widget.itemDeletesDraged) {
										int i_320_ = anInt1110;
										int i_321_ = anInt1091;
										widget.items[i_321_] = widget.items[i_320_];
										widget.itemAmounts[i_321_] = widget.itemAmounts[i_320_];
										widget.items[i_320_] = -1;
										widget.itemAmounts[i_320_] = 0;
									} else if (i_319_ == 1) {
										int i_322_ = anInt1110;
										int i_323_ = anInt1091;
										while (i_322_ != i_323_) {
											if (i_322_ > i_323_) {
												widget.swapItems(i_322_, i_322_ - 1);
												i_322_--;
											} else if (i_322_ < i_323_) {
												widget.swapItems(i_322_, i_322_ + 1);
												i_322_++;
											}
										}
									} else {
										widget.swapItems(anInt1110, anInt1091);
									}
									outBuffer.putOpcode(214);
									outBuffer.putLEShortA(anInt1109);
									outBuffer.putByteC(i_319_);
									outBuffer.putLEShortA(anInt1110);
									outBuffer.putLEShort(anInt1091);
								}
							} else if ((anInt1278 == 1 || method17(9, menuActionRow - 1)) && menuActionRow > 2) {
								processMenuHovering();
							} else if (menuActionRow > 0) {
								processMenuAction(menuActionRow - 1);
							}
							anInt1268 = 10;
							clickType = 0;
						}
					}
					if (Scene.anInt550 != -1) {
						int i_324_ = Scene.anInt550;
						int i_325_ = Scene.anInt551;
						boolean bool = calculatePath(0, 0, 0, 0, Client.localPlayer.pathY[0], 0, 0, i_325_,
								Client.localPlayer.pathX[0], true, i_324_);
						Scene.anInt550 = -1;
						if (bool) {
							anInt939 = clickX;
							anInt940 = clickY;
							anInt942 = 1;
							anInt941 = 0;
						}
					}
					if (clickType == 1 && aString869 != null) {
						aString869 = null;
						aBoolean1248 = true;
						clickType = 0;
					}
					method20();
					processClickingMinimap();
					processClickingTabs();
					processClickingChatSettings();
					if (mouseButtonPressed == 1 || clickType == 1) {
						anInt1238++;
					}
					if (anInt1048 == 2) {
						method108(3);
					}
					if (anInt1048 == 2 && aBoolean1185) {
						method39((byte) 5);
					}
					for (int i_326_ = 0; i_326_ < 5; i_326_++) {
						anIntArray1055[i_326_]++;
					}
					method73(732);
					idleTime++;
					if (idleTime > 4500) {
						anInt1036 = 250;
						idleTime -= 500;
						outBuffer.putOpcode(202);
					}
					anInt1013++;
					if (anInt1013 > 500) {
						anInt1013 = 0;
						int i_327_ = (int) (Math.random() * 8.0);
						if ((i_327_ & 0x1) == 1) {
							anInt1303 += anInt1304;
						}
						if ((i_327_ & 0x2) == 2) {
							anInt1156 += anInt1157;
						}
						if ((i_327_ & 0x4) == 4) {
							anInt921 += anInt922;
						}
					}
					if (anInt1303 < -50) {
						anInt1304 = 2;
					}
					if (anInt1303 > 50) {
						anInt1304 = -2;
					}
					if (anInt1156 < -55) {
						anInt1157 = 2;
					}
					if (anInt1156 > 55) {
						anInt1157 = -2;
					}
					if (anInt921 < -40) {
						anInt922 = 1;
					}
					if (anInt921 > 40) {
						anInt922 = -1;
					}
					anInt1279++;
					if (anInt1279 > 500) {
						anInt1279 = 0;
						int i_328_ = (int) (Math.random() * 8.0);
						if ((i_328_ & 0x1) == 1) {
							anInt1234 += anInt1235;
						}
						if ((i_328_ & 0x2) == 2) {
							anInt1195 += anInt1196;
						}
					}
					if (anInt1234 < -60) {
						anInt1235 = 2;
					}
					if (anInt1234 > 60) {
						anInt1235 = -2;
					}
					if (anInt1195 < -20) {
						anInt1196 = 1;
					}
					if (anInt1195 > 10) {
						anInt1196 = -1;
					}
					anInt1035++;
					if (anInt1035 > 50) {
						outBuffer.putOpcode(0);
					}
					try {
						if (bufferedConnection == null || outBuffer.offset <= 0) {
							break;
						}
						bufferedConnection.write(outBuffer.offset, outBuffer.payload, 0);
						outBuffer.offset = 0;
						anInt1035 = 0;
					} catch (IOException ioexception) {
						method68(-670);
					} catch (Exception exception) {
						logout();
					}
					break;
				}
				break;
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("71747, " + runtimeexception.toString());
				throw new RuntimeException();
			}
		} while (false);
	}

	private final void method63(int i) {
		try {
			SpawnObjectNode spawnobjectnode = (SpawnObjectNode) aLinkedList1204.getBack();
			while (i >= 0) {
				for (int i_329_ = 1; i_329_ > 0; i_329_++) {
					/* empty */
				}
			}
			for (/**/; spawnobjectnode != null; spawnobjectnode = (SpawnObjectNode) aLinkedList1204.getPrevious()) {
				if (spawnobjectnode.anInt1344 == -1) {
					spawnobjectnode.anInt1352 = 0;
					method89(spawnobjectnode);
				} else {
					spawnobjectnode.remove();
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("6061, " + i + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method64() {
		try {
			if (aProducingGraphicsBuffer1132 == null) {
				producingGraphicsBuffer = null;
				aProducingGraphicsBuffer1191 = null;
				aProducingGraphicsBuffer1189 = null;
				aProducingGraphicsBuffer1188 = null;
				currentSceneBuffer = null;
				aProducingGraphicsBuffer1148 = null;
				aProducingGraphicsBuffer1149 = null;
				aProducingGraphicsBuffer1150 = null;
				flameLeftBackground = new ProducingGraphicsBuffer(128, 265, getComponent());
				Rasterizer.resetPixels();
				flameRightBackground = new ProducingGraphicsBuffer(128, 265, getComponent());
				Rasterizer.resetPixels();
				aProducingGraphicsBuffer1132 = new ProducingGraphicsBuffer(509, 171, getComponent());
				Rasterizer.resetPixels();
				aProducingGraphicsBuffer1133 = new ProducingGraphicsBuffer(360, 132, getComponent());
				Rasterizer.resetPixels();
				aProducingGraphicsBuffer1134 = new ProducingGraphicsBuffer(360, 200, getComponent());
				Rasterizer.resetPixels();
				aProducingGraphicsBuffer1137 = new ProducingGraphicsBuffer(202, 238, getComponent());
				Rasterizer.resetPixels();
				aProducingGraphicsBuffer1138 = new ProducingGraphicsBuffer(203, 238, getComponent());
				Rasterizer.resetPixels();
				aProducingGraphicsBuffer1139 = new ProducingGraphicsBuffer(74, 94, getComponent());
				Rasterizer.resetPixels();
				aProducingGraphicsBuffer1140 = new ProducingGraphicsBuffer(75, 94, getComponent());
				Rasterizer.resetPixels();
				if (anArchive1078 != null) {
					prepareTitleBackground();
					prepareTitle();
				}
				drawGameframe = true;
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("33128,  " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	@Override
	public final void drawLoadingText(int i, String string) {
		do {
			anInt1104 = i;
			aString1074 = string;
			method64();
			if (anArchive1078 == null) {
				super.drawLoadingText(i, string);
			} else {
				aProducingGraphicsBuffer1134.createRasterizer();
				int i_330_ = 360;
				int i_331_ = 200;
				int i_332_ = 20;
				fontBold.drawString("RuneScape is loading - please wait...", i_330_ / 2, i_331_ / 2 - 26 - i_332_,
						0xFFFFFF);
				int i_333_ = i_331_ / 2 - 18 - i_332_;
				Rasterizer.drawUnfilledRectangle(i_330_ / 2 - 152, i_333_, 304, 34, 9179409);
				Rasterizer.drawUnfilledRectangle(i_330_ / 2 - 151, i_333_ + 1, 302, 32, 0);
				Rasterizer.drawFilledRectangle(i_330_ / 2 - 150, i_333_ + 2, i * 3, 30, 9179409);
				Rasterizer.drawFilledRectangle(i_330_ / 2 - 150 + i * 3, i_333_ + 2, 300 - i * 3, 30, 0);
				fontBold.drawString(string, i_330_ / 2, i_331_ / 2 + 5 - i_332_, 0xFFFFFF);
				aProducingGraphicsBuffer1134.drawGraphics(202, 171, gameGraphics);
				if (!drawGameframe) {
					break;
				}
				drawGameframe = false;
				if (!aBoolean856) {
					flameLeftBackground.drawGraphics(0, 0, gameGraphics);
					flameRightBackground.drawGraphics(637, 0, gameGraphics);
				}
				aProducingGraphicsBuffer1132.drawGraphics(128, 0, gameGraphics);
				aProducingGraphicsBuffer1133.drawGraphics(202, 371, gameGraphics);
				aProducingGraphicsBuffer1137.drawGraphics(0, 265, gameGraphics);
				aProducingGraphicsBuffer1138.drawGraphics(562, 265, gameGraphics);
				aProducingGraphicsBuffer1139.drawGraphics(128, 171, gameGraphics);
				aProducingGraphicsBuffer1140.drawGraphics(562, 171, gameGraphics);
			}
			break;
		} while (false);
	}

	public final void method65(int i, int i_335_, int i_336_, int i_337_, Widget widget, int i_338_, boolean bool,
			int i_339_, int i_340_) {
		do {
			try {
				if (aBoolean997) {
					anInt1017 = 32;
				} else {
					anInt1017 = 0;
				}
				aBoolean997 = false;
				packetSize += i_340_;
				if (i_336_ >= i && i_336_ < i + 16 && i_337_ >= i_338_ && i_337_ < i_338_ + 16) {
					widget.scrollPosition -= anInt1238 * 4;
					if (bool) {
						drawTabArea = true;
					}
				} else if (i_336_ >= i && i_336_ < i + 16 && i_337_ >= i_338_ + i_335_ - 16 && i_337_ < i_338_ + i_335_) {
					widget.scrollPosition += anInt1238 * 4;
					if (bool) {
						drawTabArea = true;
					}
				} else {
					if (i_336_ < i - anInt1017 || i_336_ >= i + 16 + anInt1017 || i_337_ < i_338_ + 16
							|| i_337_ >= i_338_ + i_335_ - 16 || anInt1238 <= 0) {
						break;
					}
					int i_341_ = (i_335_ - 32) * i_335_ / i_339_;
					if (i_341_ < 8) {
						i_341_ = 8;
					}
					int i_342_ = i_337_ - i_338_ - 16 - i_341_ / 2;
					int i_343_ = i_335_ - 32 - i_341_;
					widget.scrollPosition = (i_339_ - i_335_) * i_342_ / i_343_;
					if (bool) {
						drawTabArea = true;
					}
					aBoolean997 = true;
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("45715, " + i + ", " + i_335_ + ", " + i_336_ + ", " + i_337_ + ", " + widget
						+ ", " + i_338_ + ", " + bool + ", " + i_339_ + ", " + i_340_ + ", "
						+ runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final boolean method66(int i, int i_344_, int i_345_, int i_346_) {
		try {
			int i_347_ = i >> 14 & 0x7fff;
			int i_348_ = currentScene.method526(currentSceneIndex, i_345_, i_344_, i);
			if (i_346_ >= 0) {
				throw new NullPointerException();
			}
			if (i_348_ == -1) {
				return false;
			}
			int i_349_ = i_348_ & 0x1f;
			int i_350_ = i_348_ >> 6 & 0x3;
			if (i_349_ == 10 || i_349_ == 11 || i_349_ == 22) {
				GameObjectDefinition gameobjectdefinition = GameObjectDefinition.getDefinition(i_347_);
				int i_351_;
				int i_352_;
				if (i_350_ == 0 || i_350_ == 2) {
					i_351_ = gameobjectdefinition.width;
					i_352_ = gameobjectdefinition.height;
				} else {
					i_351_ = gameobjectdefinition.height;
					i_352_ = gameobjectdefinition.width;
				}
				int i_353_ = gameobjectdefinition.anInt273;
				if (i_350_ != 0) {
					i_353_ = (i_353_ << i_350_ & 0xf) + (i_353_ >> 4 - i_350_);
				}
				calculatePath(2, 0, i_352_, 0, Client.localPlayer.pathY[0], i_351_, i_353_, i_344_,
						Client.localPlayer.pathX[0], false, i_345_);
			} else {
				calculatePath(2, i_350_, 0, i_349_ + 1, Client.localPlayer.pathY[0], 0, 0, i_344_,
						Client.localPlayer.pathX[0], false, i_345_);
			}
			anInt939 = clickX;
			anInt940 = clickY;
			anInt942 = 2;
			anInt941 = 0;
			return true;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("61218, " + i + ", " + i_344_ + ", " + i_345_ + ", " + i_346_ + ", "
					+ runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final Archive requestArchive(int index, String archiveAlias, String archiveName, int archiveCRC,
			int loadPercentage) {
		try {
			byte[] archiveBuffer = null;
			int retryTime = 5;
			try {
				if (stores[0] != null) {
					archiveBuffer = stores[0].get(index);
				}
			} catch (Exception exception) {
				/* empty */
			}
			if (Client.JAGGRAB) {
				if (archiveBuffer != null) {
					crc32.reset();
					crc32.update(archiveBuffer);
					int crc = (int) crc32.getValue();
					if (crc != archiveCRC) {
						archiveBuffer = null;
					}
				}
			}
			if (archiveBuffer != null) {
				Archive archive = new Archive(archiveBuffer);
				return archive;
			}
			int errors = 0;
			while (archiveBuffer == null) {
				String message = "Unknown error";
				drawLoadingText(loadPercentage, "Requesting " + archiveAlias);
				try {
					int i_361_ = 0;
					DataInputStream in = jaggrabRequest(archiveName + archiveCRC);
					byte[] responseBuffer = new byte[6];
					in.readFully(responseBuffer, 0, 6);
					Buffer buffer = new Buffer(responseBuffer);
					buffer.offset = 3;
					int i_363_ = buffer.get24BitInt() + 6;
					int i_364_ = 6;
					archiveBuffer = new byte[i_363_];
					for (int i = 0; i < 6; i++) {
						archiveBuffer[i] = responseBuffer[i];
					}
					while (i_364_ < i_363_) {
						int i_366_ = i_363_ - i_364_;
						if (i_366_ > 1000) {
							i_366_ = 1000;
						}
						int i_367_ = in.read(archiveBuffer, i_364_, i_366_);
						if (i_367_ < 0) {
							message = "Length error: " + i_364_ + "/" + i_363_;
							throw new IOException("EOF");
						}
						i_364_ += i_367_;
						int i_368_ = i_364_ * 100 / i_363_;
						if (i_368_ != i_361_) {
							drawLoadingText(loadPercentage, "Loading " + archiveAlias + " - " + i_368_ + "%");
						}
						i_361_ = i_368_;
					}
					in.close();
					try {
						if (stores[0] != null) {
							stores[0].put(archiveBuffer.length, archiveBuffer, index);
						}
					} catch (Exception exception) {
						stores[0] = null;
					}
					if (Client.JAGGRAB) {
						if (archiveBuffer != null) {
							crc32.reset();
							crc32.update(archiveBuffer);
							int crc = (int) crc32.getValue();
							if (crc != archiveCRC) {
								archiveBuffer = null;
								errors++;
								message = "Checksum error: " + crc;
							}
						}
					}
				} catch (IOException ioexception) {
					if (message.equals("Unknown error")) {
						message = "Connection error";
					}
					archiveBuffer = null;
				} catch (NullPointerException nullpointerexception) {
					message = "Null error";
					archiveBuffer = null;
					if (!SignLink.accessible) {
						return null;
					}
				} catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
					message = "Bounds error";
					archiveBuffer = null;
					if (!SignLink.accessible) {
						return null;
					}
				} catch (Exception exception) {
					message = "Unexpected error";
					archiveBuffer = null;
					if (!SignLink.accessible) {
						return null;
					}
				}
				if (archiveBuffer == null) {
					for (int waitTime = retryTime; waitTime > 0; waitTime--) {
						if (errors >= 3) {
							drawLoadingText(loadPercentage, "Game updated - please reload page");
							waitTime = 10;
						} else {
							drawLoadingText(loadPercentage, message + " - Retrying in " + waitTime);
						}
						try {
							Thread.sleep(1000L);
						} catch (Exception exception) {
							/* empty */
						}
					}
					retryTime *= 2;
					if (retryTime > 60) {
						retryTime = 60;
					}
					aBoolean897 = !aBoolean897;
				}
			}
			return new Archive(archiveBuffer);
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("44021, " + index + ", " + archiveAlias + ", " + archiveName + ", " + archiveCRC
					+ ", " + loadPercentage + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method68(int i) {
		try {
			if (anInt1036 > 0) {
				logout();
			} else {
				currentSceneBuffer.createRasterizer();
				fontNormal.drawString("Connection lost", 257, 144, 0);
				fontNormal.drawString("Connection lost", 256, 143, 0xFFFFFF);
				fontNormal.drawString("Please wait - attempting to reestablish", 257, 159, 0);
				fontNormal.drawString("Please wait - attempting to reestablish", 256, 158, 0xFFFFFF);
				while (i >= 0) {
					outBuffer.put(164);
				}
				currentSceneBuffer.drawGraphics(4, 4, gameGraphics);
				minimapState = 0;
				destinationX = 0;
				BufferedConnection bufferedconnection = bufferedConnection;
				loggedIn = false;
				anInt1063 = 0;
				method84(aString1198, aString1199, true);
				if (!loggedIn) {
					logout();
				}
				try {
					bufferedconnection.close();
				} catch (Exception exception) {
					/* empty */
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("43851, " + i + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void processMenuAction(int pressedRow) {
		try {
			if (pressedRow >= 0) {
				if (inputType != 0) {
					inputType = 0;
					aBoolean1248 = true;
				}
				int actionIndex1 = menuActionIndex2[pressedRow];
				int actionIndex2 = menuActionIndex3[pressedRow];
				int menuActionIndex = menuActionIndexes[pressedRow];
				int actionIndex3 = menuActionIndex1[pressedRow];
				if (menuActionIndex >= 2000) {
					menuActionIndex -= 2000;
				}
				if (menuActionIndex == 582) {
					Npc npc = actors[actionIndex3];
					if (npc != null) {
						calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, npc.pathY[0],
								Client.localPlayer.pathX[0], false, npc.pathX[0]);
						anInt939 = clickX;
						anInt940 = clickY;
						anInt942 = 2;
						anInt941 = 0;
						outBuffer.putOpcode(57);
						outBuffer.putShortA(anInt1310);
						outBuffer.putShortA(actionIndex3);
						outBuffer.putLEShort(anInt1308);
						outBuffer.putShortA(anInt1309);
					}
				}
				if (menuActionIndex == 234) {
					boolean bool_375_ = calculatePath(2, 0, 0, 0, Client.localPlayer.pathY[0], 0, 0, actionIndex2,
							Client.localPlayer.pathX[0], false, actionIndex1);
					if (!bool_375_) {
						bool_375_ = calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, actionIndex2,
								Client.localPlayer.pathX[0], false, actionIndex1);
					}
					anInt939 = clickX;
					anInt940 = clickY;
					anInt942 = 2;
					anInt941 = 0;
					outBuffer.putOpcode(236);
					outBuffer.putLEShort(actionIndex2 + regionAbsoluteBaseY);
					outBuffer.putShort(actionIndex3);
					outBuffer.putLEShort(actionIndex1 + regionAbsoluteBaseX);
				}
				if (menuActionIndex == 62 && method66(actionIndex3, actionIndex2, actionIndex1, -770)) {
					outBuffer.putOpcode(192);
					outBuffer.putShort(anInt1309);
					outBuffer.putLEShort(actionIndex3 >> 14 & 0x7fff);
					outBuffer.putLEShortA(actionIndex2 + regionAbsoluteBaseY);
					outBuffer.putLEShort(anInt1308);
					outBuffer.putLEShortA(actionIndex1 + regionAbsoluteBaseX);
					outBuffer.putShort(anInt1310);
				}
				if (menuActionIndex == 511) {
					boolean bool_376_ = calculatePath(2, 0, 0, 0, Client.localPlayer.pathY[0], 0, 0, actionIndex2,
							Client.localPlayer.pathX[0], false, actionIndex1);
					if (!bool_376_) {
						bool_376_ = calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, actionIndex2,
								Client.localPlayer.pathX[0], false, actionIndex1);
					}
					anInt939 = clickX;
					anInt940 = clickY;
					anInt942 = 2;
					anInt941 = 0;
					outBuffer.putOpcode(25);
					outBuffer.putLEShort(anInt1309);
					outBuffer.putShortA(anInt1310);
					outBuffer.putShort(actionIndex3);
					outBuffer.putShortA(actionIndex2 + regionAbsoluteBaseY);
					outBuffer.putLEShortA(anInt1308);
					outBuffer.putShort(actionIndex1 + regionAbsoluteBaseX);
				}
				if (menuActionIndex == 74) {
					outBuffer.putOpcode(122);
					outBuffer.putLEShortA(actionIndex2);
					outBuffer.putShortA(actionIndex1);
					outBuffer.putLEShort(actionIndex3);
					anInt1268 = 0;
					anInt1269 = actionIndex2;
					anInt1270 = actionIndex1;
					anInt1271 = 2;
					if (Widget.cache[actionIndex2].parentIndex == openWidgetIndex) {
						anInt1271 = 1;
					}
					if (Widget.cache[actionIndex2].parentIndex == chatboxWidgetIndex) {
						anInt1271 = 3;
					}
				}
				if (menuActionIndex == 315) {
					Widget widget = Widget.cache[actionIndex2];
					boolean bool_377_ = true;
					if (widget.contentType > 0) {
						bool_377_ = method48(widget);
					}
					if (bool_377_) {
						outBuffer.putOpcode(185);
						outBuffer.putShort(actionIndex2);
					}
				}
				if (menuActionIndex == 561) {
					Player player = players[actionIndex3];
					if (player != null) {
						calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, player.pathY[0],
								Client.localPlayer.pathX[0], false, player.pathX[0]);
						anInt939 = clickX;
						anInt940 = clickY;
						anInt942 = 2;
						anInt941 = 0;
						Client.anInt1213 += actionIndex3;
						if (Client.anInt1213 >= 90) {
							outBuffer.putOpcode(136);
							Client.anInt1213 = 0;
						}
						outBuffer.putOpcode(128);
						outBuffer.putShort(actionIndex3);
					}
				}
				if (menuActionIndex == 20) {
					Npc npc = actors[actionIndex3];
					if (npc != null) {
						calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, npc.pathY[0],
								Client.localPlayer.pathX[0], false, npc.pathX[0]);
						anInt939 = clickX;
						anInt940 = clickY;
						anInt942 = 2;
						anInt941 = 0;
						outBuffer.putOpcode(155);
						outBuffer.putLEShort(actionIndex3);
					}
				}
				if (menuActionIndex == 779) {
					Player player = players[actionIndex3];
					if (player != null) {
						calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, player.pathY[0],
								Client.localPlayer.pathX[0], false, player.pathX[0]);
						anInt939 = clickX;
						anInt940 = clickY;
						anInt942 = 2;
						anInt941 = 0;
						outBuffer.putOpcode(153);
						outBuffer.putLEShort(actionIndex3);
					}
				}
				if (menuActionIndex == 516) {
					if (!actionMenuOpen) {
						currentScene.method534(false, clickY - 4, clickX - 4);
					} else {
						currentScene.method534(false, actionIndex2 - 4, actionIndex1 - 4);
					}
				}
				if (menuActionIndex == 1062) {
					Client.anInt949 += regionAbsoluteBaseX;
					if (Client.anInt949 >= 113) {
						outBuffer.putOpcode(183);
						outBuffer.put24BitInt(15086193);
						Client.anInt949 = 0;
					}
					method66(actionIndex3, actionIndex2, actionIndex1, -770);
					outBuffer.putOpcode(228);
					outBuffer.putShortA(actionIndex3 >> 14 & 0x7fff);
					outBuffer.putShortA(actionIndex2 + regionAbsoluteBaseY);
					outBuffer.putShort(actionIndex1 + regionAbsoluteBaseX);
				}
				if (menuActionIndex == 679 && !aBoolean1174) {
					outBuffer.putOpcode(40);
					outBuffer.putShort(actionIndex2);
					aBoolean1174 = true;
				}
				if (menuActionIndex == 431) {
					outBuffer.putOpcode(129);
					outBuffer.putShortA(actionIndex1);
					outBuffer.putShort(actionIndex2);
					outBuffer.putShortA(actionIndex3);
					anInt1268 = 0;
					anInt1269 = actionIndex2;
					anInt1270 = actionIndex1;
					anInt1271 = 2;
					if (Widget.cache[actionIndex2].parentIndex == openWidgetIndex) {
						anInt1271 = 1;
					}
					if (Widget.cache[actionIndex2].parentIndex == chatboxWidgetIndex) {
						anInt1271 = 3;
					}
				}
				if (menuActionIndex == 337 || menuActionIndex == 42 || menuActionIndex == 792 || menuActionIndex == 322) {
					String string = menuActionNames[pressedRow];
					int i_378_ = string.indexOf("@whi@");
					if (i_378_ != -1) {
						long l = TextUtils.nameToLong(string.substring(i_378_ + 5).trim());
						if (menuActionIndex == 337) {
							method41((byte) 68, l);
						}
						if (menuActionIndex == 42) {
							method113(l, 4);
						}
						if (menuActionIndex == 792) {
							method35(false, l);
						}
						if (menuActionIndex == 322) {
							method122(3, l);
						}
					}
				}
				if (menuActionIndex == 53) {
					outBuffer.putOpcode(135);
					outBuffer.putLEShort(actionIndex1);
					outBuffer.putShortA(actionIndex2);
					outBuffer.putLEShort(actionIndex3);
					anInt1268 = 0;
					anInt1269 = actionIndex2;
					anInt1270 = actionIndex1;
					anInt1271 = 2;
					if (Widget.cache[actionIndex2].parentIndex == openWidgetIndex) {
						anInt1271 = 1;
					}
					if (Widget.cache[actionIndex2].parentIndex == chatboxWidgetIndex) {
						anInt1271 = 3;
					}
				}
				if (menuActionIndex == 539) {
					outBuffer.putOpcode(16);
					outBuffer.putShortA(actionIndex3);
					outBuffer.putLEShortA(actionIndex1);
					outBuffer.putLEShortA(actionIndex2);
					anInt1268 = 0;
					anInt1269 = actionIndex2;
					anInt1270 = actionIndex1;
					anInt1271 = 2;
					if (Widget.cache[actionIndex2].parentIndex == openWidgetIndex) {
						anInt1271 = 1;
					}
					if (Widget.cache[actionIndex2].parentIndex == chatboxWidgetIndex) {
						anInt1271 = 3;
					}
				}
				if (menuActionIndex == 484 || menuActionIndex == 6) {
					String string = menuActionNames[pressedRow];
					int i_379_ = string.indexOf("@whi@");
					if (i_379_ != -1) {
						string = string.substring(i_379_ + 5).trim();
						String string_380_ = TextUtils.formatName(TextUtils.longToName(TextUtils.nameToLong(string)));
						boolean bool_381_ = false;
						for (int i_382_ = 0; i_382_ < playerCount; i_382_++) {
							Player player = players[anIntArray917[i_382_]];
							if (player != null && player.playerName != null
									&& player.playerName.equalsIgnoreCase(string_380_)) {
								calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, player.pathY[0],
										Client.localPlayer.pathX[0], false, player.pathX[0]);
								if (menuActionIndex == 484) {
									outBuffer.putOpcode(139);
									outBuffer.putLEShort(anIntArray917[i_382_]);
								}
								if (menuActionIndex == 6) {
									Client.anInt1213 += actionIndex3;
									if (Client.anInt1213 >= 90) {
										outBuffer.putOpcode(136);
										Client.anInt1213 = 0;
									}
									outBuffer.putOpcode(128);
									outBuffer.putShort(anIntArray917[i_382_]);
								}
								bool_381_ = true;
								break;
							}
						}
						if (!bool_381_) {
							sendMessage("Unable to find " + string_380_, 0, "");
						}
					}
				}
				if (menuActionIndex == 870) {
					outBuffer.putOpcode(53);
					outBuffer.putShort(actionIndex1);
					outBuffer.putShortA(anInt1308);
					outBuffer.putLEShortA(actionIndex3);
					outBuffer.putShort(anInt1309);
					outBuffer.putLEShort(anInt1310);
					outBuffer.putShort(actionIndex2);
					anInt1268 = 0;
					anInt1269 = actionIndex2;
					anInt1270 = actionIndex1;
					anInt1271 = 2;
					if (Widget.cache[actionIndex2].parentIndex == openWidgetIndex) {
						anInt1271 = 1;
					}
					if (Widget.cache[actionIndex2].parentIndex == chatboxWidgetIndex) {
						anInt1271 = 3;
					}
				}
				if (menuActionIndex == 847) {
					outBuffer.putOpcode(87);
					outBuffer.putShortA(actionIndex3);
					outBuffer.putShort(actionIndex2);
					outBuffer.putShortA(actionIndex1);
					anInt1268 = 0;
					anInt1269 = actionIndex2;
					anInt1270 = actionIndex1;
					anInt1271 = 2;
					if (Widget.cache[actionIndex2].parentIndex == openWidgetIndex) {
						anInt1271 = 1;
					}
					if (Widget.cache[actionIndex2].parentIndex == chatboxWidgetIndex) {
						anInt1271 = 3;
					}
				}
				if (menuActionIndex == 626) {
					Widget widget = Widget.cache[actionIndex2];
					anInt1161 = 1;
					spellId = actionIndex2;
					anInt1163 = widget.spellUsableOn;
					anInt1307 = 0;
					drawTabArea = true;
					String string = widget.selectedActionName;
					if (string.indexOf(" ") != -1) {
						string = string.substring(0, string.indexOf(" "));
					}
					String string_383_ = widget.selectedActionName;
					if (string_383_.indexOf(" ") != -1) {
						string_383_ = string_383_.substring(string_383_.indexOf(" ") + 1);
					}
					aString1164 = string + " " + widget.spellName + " " + string_383_;
					if (anInt1163 == 16) {
						drawTabArea = true;
						currentTabIndex = 3;
						aBoolean1128 = true;
					}
				} else {
					if (menuActionIndex == 78) {
						outBuffer.putOpcode(117);
						outBuffer.putLEShortA(actionIndex2);
						outBuffer.putLEShortA(actionIndex3);
						outBuffer.putLEShort(actionIndex1);
						anInt1268 = 0;
						anInt1269 = actionIndex2;
						anInt1270 = actionIndex1;
						anInt1271 = 2;
						if (Widget.cache[actionIndex2].parentIndex == openWidgetIndex) {
							anInt1271 = 1;
						}
						if (Widget.cache[actionIndex2].parentIndex == chatboxWidgetIndex) {
							anInt1271 = 3;
						}
					}
					if (menuActionIndex == 27) {
						Player player = players[actionIndex3];
						if (player != null) {
							calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, player.pathY[0],
									Client.localPlayer.pathX[0], false, player.pathX[0]);
							anInt939 = clickX;
							anInt940 = clickY;
							anInt942 = 2;
							anInt941 = 0;
							Client.anInt1011 += actionIndex3;
							if (Client.anInt1011 >= 54) {
								outBuffer.putOpcode(189);
								outBuffer.put(234);
								Client.anInt1011 = 0;
							}
							outBuffer.putOpcode(73);
							outBuffer.putLEShort(actionIndex3);
						}
					}
					if (menuActionIndex == 213) {
						boolean bool_384_ = calculatePath(2, 0, 0, 0, Client.localPlayer.pathY[0], 0, 0, actionIndex2,
								Client.localPlayer.pathX[0], false, actionIndex1);
						if (!bool_384_) {
							bool_384_ = calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, actionIndex2,
									Client.localPlayer.pathX[0], false, actionIndex1);
						}
						anInt939 = clickX;
						anInt940 = clickY;
						anInt942 = 2;
						anInt941 = 0;
						outBuffer.putOpcode(79);
						outBuffer.putLEShort(actionIndex2 + regionAbsoluteBaseY);
						outBuffer.putShort(actionIndex3);
						outBuffer.putShortA(actionIndex1 + regionAbsoluteBaseX);
					}
					if (menuActionIndex == 632) {
						outBuffer.putOpcode(145);
						outBuffer.putShortA(actionIndex2);
						outBuffer.putShortA(actionIndex1);
						outBuffer.putShortA(actionIndex3);
						anInt1268 = 0;
						anInt1269 = actionIndex2;
						anInt1270 = actionIndex1;
						anInt1271 = 2;
						if (Widget.cache[actionIndex2].parentIndex == openWidgetIndex) {
							anInt1271 = 1;
						}
						if (Widget.cache[actionIndex2].parentIndex == chatboxWidgetIndex) {
							anInt1271 = 3;
						}
					}
					if (menuActionIndex == 493) {
						outBuffer.putOpcode(75);
						outBuffer.putLEShortA(actionIndex2);
						outBuffer.putLEShort(actionIndex1);
						outBuffer.putShortA(actionIndex3);
						anInt1268 = 0;
						anInt1269 = actionIndex2;
						anInt1270 = actionIndex1;
						anInt1271 = 2;
						if (Widget.cache[actionIndex2].parentIndex == openWidgetIndex) {
							anInt1271 = 1;
						}
						if (Widget.cache[actionIndex2].parentIndex == chatboxWidgetIndex) {
							anInt1271 = 3;
						}
					}
					if (menuActionIndex == 652) {
						boolean bool_385_ = calculatePath(2, 0, 0, 0, Client.localPlayer.pathY[0], 0, 0, actionIndex2,
								Client.localPlayer.pathX[0], false, actionIndex1);
						if (!bool_385_) {
							bool_385_ = calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, actionIndex2,
									Client.localPlayer.pathX[0], false, actionIndex1);
						}
						anInt939 = clickX;
						anInt940 = clickY;
						anInt942 = 2;
						anInt941 = 0;
						outBuffer.putOpcode(156);
						outBuffer.putShortA(actionIndex1 + regionAbsoluteBaseX);
						outBuffer.putLEShort(actionIndex2 + regionAbsoluteBaseY);
						outBuffer.putLEShortA(actionIndex3);
					}
					if (menuActionIndex == 94) {
						boolean bool_386_ = calculatePath(2, 0, 0, 0, Client.localPlayer.pathY[0], 0, 0, actionIndex2,
								Client.localPlayer.pathX[0], false, actionIndex1);
						if (!bool_386_) {
							bool_386_ = calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, actionIndex2,
									Client.localPlayer.pathX[0], false, actionIndex1);
						}
						anInt939 = clickX;
						anInt940 = clickY;
						anInt942 = 2;
						anInt941 = 0;
						outBuffer.putOpcode(181);
						outBuffer.putLEShort(actionIndex2 + regionAbsoluteBaseY);
						outBuffer.putShort(actionIndex3);
						outBuffer.putLEShort(actionIndex1 + regionAbsoluteBaseX);
						outBuffer.putShortA(spellId);
					}
					if (menuActionIndex == 646) {
						outBuffer.putOpcode(185);
						outBuffer.putShort(actionIndex2);
						Widget widget = Widget.cache[actionIndex2];
						if (widget.opcodes != null && widget.opcodes[0][0] == 5) {
							int i_387_ = widget.opcodes[0][1];
							if (settings[i_387_] != widget.conditionValues[0]) {
								settings[i_387_] = widget.conditionValues[0];
								method33(i_387_);
								drawTabArea = true;
							}
						}
					}
					if (menuActionIndex == 225) {
						Npc npc = actors[actionIndex3];
						if (npc != null) {
							calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, npc.pathY[0],
									Client.localPlayer.pathX[0], false, npc.pathX[0]);
							anInt939 = clickX;
							anInt940 = clickY;
							anInt942 = 2;
							anInt941 = 0;
							Client.anInt1251 += actionIndex3;
							if (Client.anInt1251 >= 85) {
								outBuffer.putOpcode(230);
								outBuffer.put(239);
								Client.anInt1251 = 0;
							}
							outBuffer.putOpcode(17);
							outBuffer.putLEShortA(actionIndex3);
						}
					}
					if (menuActionIndex == 965) {
						Npc npc = actors[actionIndex3];
						if (npc != null) {
							calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, npc.pathY[0],
									Client.localPlayer.pathX[0], false, npc.pathX[0]);
							anInt939 = clickX;
							anInt940 = clickY;
							anInt942 = 2;
							anInt941 = 0;
							Client.anInt1159++;
							if (Client.anInt1159 >= 96) {
								outBuffer.putOpcode(152);
								outBuffer.put(88);
								Client.anInt1159 = 0;
							}
							outBuffer.putOpcode(21);
							outBuffer.putShort(actionIndex3);
						}
					}
					if (menuActionIndex == 413) {
						Npc npc = actors[actionIndex3];
						if (npc != null) {
							calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, npc.pathY[0],
									Client.localPlayer.pathX[0], false, npc.pathX[0]);
							anInt939 = clickX;
							anInt940 = clickY;
							anInt942 = 2;
							anInt941 = 0;
							outBuffer.putOpcode(131);
							outBuffer.putLEShortA(actionIndex3);
							outBuffer.putShortA(spellId);
						}
					}
					if (menuActionIndex == 200) {
						closeWidgets();
					}
					if (menuActionIndex == 1025) {
						Npc npc = actors[actionIndex3];
						if (npc != null) {
							ActorDefinition npcdefinition = npc.npcDefinition;
							if (npcdefinition.childrenIndexes != null) {
								npcdefinition = npcdefinition.getChildDefinition();
							}
							if (npcdefinition != null) {
								String string;
								if (npcdefinition.description != null) {
									string = new String(npcdefinition.description);
								} else {
									string = "It's a " + npcdefinition.name + ".";
								}
								sendMessage(string, 0, "");
							}
						}
					}
					if (menuActionIndex == 900) {
						method66(actionIndex3, actionIndex2, actionIndex1, -770);
						outBuffer.putOpcode(252);
						outBuffer.putLEShortA(actionIndex3 >> 14 & 0x7fff);
						outBuffer.putLEShort(actionIndex2 + regionAbsoluteBaseY);
						outBuffer.putShortA(actionIndex1 + regionAbsoluteBaseX);
					}
					if (menuActionIndex == 412) {
						Npc npc = actors[actionIndex3];
						if (npc != null) {
							calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, npc.pathY[0],
									Client.localPlayer.pathX[0], false, npc.pathX[0]);
							anInt939 = clickX;
							anInt940 = clickY;
							anInt942 = 2;
							anInt941 = 0;
							outBuffer.putOpcode(72);
							outBuffer.putShortA(actionIndex3);
						}
					}
					if (menuActionIndex == 365) {
						Player player = players[actionIndex3];
						if (player != null) {
							calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, player.pathY[0],
									Client.localPlayer.pathX[0], false, player.pathX[0]);
							anInt939 = clickX;
							anInt940 = clickY;
							anInt942 = 2;
							anInt941 = 0;
							outBuffer.putOpcode(249);
							outBuffer.putShortA(actionIndex3);
							outBuffer.putLEShort(spellId);
						}
					}
					if (menuActionIndex == 729) {
						Player player = players[actionIndex3];
						if (player != null) {
							calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, player.pathY[0],
									Client.localPlayer.pathX[0], false, player.pathX[0]);
							anInt939 = clickX;
							anInt940 = clickY;
							anInt942 = 2;
							anInt941 = 0;
							outBuffer.putOpcode(39);
							outBuffer.putLEShort(actionIndex3);
						}
					}
					if (menuActionIndex == 577) {
						Player player = players[actionIndex3];
						if (player != null) {
							calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, player.pathY[0],
									Client.localPlayer.pathX[0], false, player.pathX[0]);
							anInt939 = clickX;
							anInt940 = clickY;
							anInt942 = 2;
							anInt941 = 0;
							outBuffer.putOpcode(139);
							outBuffer.putLEShort(actionIndex3);
						}
					}
					if (menuActionIndex == 956 && method66(actionIndex3, actionIndex2, actionIndex1, -770)) {
						outBuffer.putOpcode(35);
						outBuffer.putLEShort(actionIndex1 + regionAbsoluteBaseX);
						outBuffer.putShortA(spellId);
						outBuffer.putShortA(actionIndex2 + regionAbsoluteBaseY);
						outBuffer.putLEShort(actionIndex3 >> 14 & 0x7fff);
					}
					if (menuActionIndex == 567) {
						boolean bool_388_ = calculatePath(2, 0, 0, 0, Client.localPlayer.pathY[0], 0, 0, actionIndex2,
								Client.localPlayer.pathX[0], false, actionIndex1);
						if (!bool_388_) {
							bool_388_ = calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, actionIndex2,
									Client.localPlayer.pathX[0], false, actionIndex1);
						}
						anInt939 = clickX;
						anInt940 = clickY;
						anInt942 = 2;
						anInt941 = 0;
						outBuffer.putOpcode(23);
						outBuffer.putLEShort(actionIndex2 + regionAbsoluteBaseY);
						outBuffer.putLEShort(actionIndex3);
						outBuffer.putLEShort(actionIndex1 + regionAbsoluteBaseX);
					}
					if (menuActionIndex == 867) {
						if ((actionIndex3 & 0x3) == 0) {
							Client.anInt1200++;
						}
						if (Client.anInt1200 >= 59) {
							outBuffer.putOpcode(200);
							outBuffer.putShort(25501);
							Client.anInt1200 = 0;
						}
						outBuffer.putOpcode(43);
						outBuffer.putLEShort(actionIndex2);
						outBuffer.putShortA(actionIndex3);
						outBuffer.putShortA(actionIndex1);
						anInt1268 = 0;
						anInt1269 = actionIndex2;
						anInt1270 = actionIndex1;
						anInt1271 = 2;
						if (Widget.cache[actionIndex2].parentIndex == openWidgetIndex) {
							anInt1271 = 1;
						}
						if (Widget.cache[actionIndex2].parentIndex == chatboxWidgetIndex) {
							anInt1271 = 3;
						}
					}
					if (menuActionIndex == 543) {
						outBuffer.putOpcode(237);
						outBuffer.putShort(actionIndex1);
						outBuffer.putShortA(actionIndex3);
						outBuffer.putShort(actionIndex2);
						outBuffer.putShortA(spellId);
						anInt1268 = 0;
						anInt1269 = actionIndex2;
						anInt1270 = actionIndex1;
						anInt1271 = 2;
						if (Widget.cache[actionIndex2].parentIndex == openWidgetIndex) {
							anInt1271 = 1;
						}
						if (Widget.cache[actionIndex2].parentIndex == chatboxWidgetIndex) {
							anInt1271 = 3;
						}
					}
					if (menuActionIndex == 606) {
						String string = menuActionNames[pressedRow];
						int i_389_ = string.indexOf("@whi@");
						if (i_389_ != -1) {
							if (openWidgetIndex == -1) {
								closeWidgets();
								reportWidgetName = string.substring(i_389_ + 5).trim();
								aBoolean1183 = false;
								for (int i_390_ = 0; i_390_ < Widget.cache.length; i_390_++) {
									if (Widget.cache[i_390_] != null && Widget.cache[i_390_].contentType == 600) {
										anInt1203 = openWidgetIndex = Widget.cache[i_390_].parentIndex;
										break;
									}
								}
							} else {
								sendMessage("Please close the interface you have open before using 'report abuse'", 0,
										"");
							}
						}
					}
					if (menuActionIndex == 491) {
						Player player = players[actionIndex3];
						if (player != null) {
							calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, player.pathY[0],
									Client.localPlayer.pathX[0], false, player.pathX[0]);
							anInt939 = clickX;
							anInt940 = clickY;
							anInt942 = 2;
							anInt941 = 0;
							outBuffer.putOpcode(14);
							outBuffer.putShortA(anInt1309);
							outBuffer.putShort(actionIndex3);
							outBuffer.putShort(anInt1310);
							outBuffer.putLEShort(anInt1308);
						}
					}
					if (menuActionIndex == 639) {
						String string = menuActionNames[pressedRow];
						int i_391_ = string.indexOf("@whi@");
						if (i_391_ != -1) {
							long l = TextUtils.nameToLong(string.substring(i_391_ + 5).trim());
							int i_392_ = -1;
							for (int i_393_ = 0; i_393_ < friendsListCount; i_393_++) {
								if (friendsListLongs[i_393_] == l) {
									i_392_ = i_393_;
									break;
								}
							}
							if (i_392_ != -1 && friendsListWorlds[i_392_] > 0) {
								aBoolean1248 = true;
								inputType = 0;
								aBoolean1281 = true;
								chatMessage = "";
								anInt1089 = 3;
								aLong978 = friendsListLongs[i_392_];
								inputCustomMessage = "Enter message to send to " + friendsListNames[i_392_];
							}
						}
					}
					if (menuActionIndex == 454) {
						outBuffer.putOpcode(41);
						outBuffer.putShort(actionIndex3);
						outBuffer.putShortA(actionIndex1);
						outBuffer.putShortA(actionIndex2);
						anInt1268 = 0;
						anInt1269 = actionIndex2;
						anInt1270 = actionIndex1;
						anInt1271 = 2;
						if (Widget.cache[actionIndex2].parentIndex == openWidgetIndex) {
							anInt1271 = 1;
						}
						if (Widget.cache[actionIndex2].parentIndex == chatboxWidgetIndex) {
							anInt1271 = 3;
						}
					}
					if (menuActionIndex == 478) {
						Npc npc = actors[actionIndex3];
						if (npc != null) {
							calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, npc.pathY[0],
									Client.localPlayer.pathX[0], false, npc.pathX[0]);
							anInt939 = clickX;
							anInt940 = clickY;
							anInt942 = 2;
							anInt941 = 0;
							if ((actionIndex3 & 0x3) == 0) {
								Client.anInt1180++;
							}
							if (Client.anInt1180 >= 53) {
								outBuffer.putOpcode(85);
								outBuffer.put(66);
								Client.anInt1180 = 0;
							}
							outBuffer.putOpcode(18);
							outBuffer.putLEShort(actionIndex3);
						}
					}
					if (menuActionIndex == 113) {
						method66(actionIndex3, actionIndex2, actionIndex1, -770);
						outBuffer.putOpcode(70);
						outBuffer.putLEShort(actionIndex1 + regionAbsoluteBaseX);
						outBuffer.putShort(actionIndex2 + regionAbsoluteBaseY);
						outBuffer.putLEShortA(actionIndex3 >> 14 & 0x7fff);
					}
					if (menuActionIndex == 872) {
						method66(actionIndex3, actionIndex2, actionIndex1, -770);
						outBuffer.putOpcode(234);
						outBuffer.putLEShortA(actionIndex1 + regionAbsoluteBaseX);
						outBuffer.putShortA(actionIndex3 >> 14 & 0x7fff);
						outBuffer.putLEShortA(actionIndex2 + regionAbsoluteBaseY);
					}
					if (menuActionIndex == 502) {
						method66(actionIndex3, actionIndex2, actionIndex1, -770);
						outBuffer.putOpcode(132);
						outBuffer.putLEShortA(actionIndex1 + regionAbsoluteBaseX);
						outBuffer.putShort(actionIndex3 >> 14 & 0x7fff);
						outBuffer.putShortA(actionIndex2 + regionAbsoluteBaseY);
					}
					if (menuActionIndex == 1125) {
						ItemDefinition itemdefinition = ItemDefinition.getDefinition(actionIndex3);
						Widget widget = Widget.cache[actionIndex2];
						String string;
						if (widget != null && widget.itemAmounts[actionIndex1] >= 100000) {
							string = String.valueOf(widget.itemAmounts[actionIndex1]) + " x " + itemdefinition.name;
						} else if (itemdefinition.description != null) {
							string = new String(itemdefinition.description);
						} else {
							string = "It's a " + itemdefinition.name + ".";
						}
						sendMessage(string, 0, "");
					}
					if (menuActionIndex == 169) {
						outBuffer.putOpcode(185);
						outBuffer.putShort(actionIndex2);
						Widget widget = Widget.cache[actionIndex2];
						if (widget.opcodes != null && widget.opcodes[0][0] == 5) {
							int i_394_ = widget.opcodes[0][1];
							settings[i_394_] = 1 - settings[i_394_];
							method33(i_394_);
							drawTabArea = true;
						}
					}
					if (menuActionIndex == 447) {
						anInt1307 = 1;
						anInt1308 = actionIndex1;
						anInt1309 = actionIndex2;
						anInt1310 = actionIndex3;
						aString1311 = ItemDefinition.getDefinition(actionIndex3).name;
						anInt1161 = 0;
						drawTabArea = true;
					} else {
						if (menuActionIndex == 1226) {
							int hash = actionIndex3 >> 14 & 0x7fff;
							GameObjectDefinition gameobjectdefinition = GameObjectDefinition.getDefinition(hash);
							String description;
							if (gameobjectdefinition.description != null) {
								description = new String(gameobjectdefinition.description);
							} else {
								description = "It's a " + gameobjectdefinition.name + ".";
							}
							sendMessage(description, 0, "");
						}
						if (menuActionIndex == 244) {
							boolean bool_396_ = calculatePath(2, 0, 0, 0, Client.localPlayer.pathY[0], 0, 0,
									actionIndex2, Client.localPlayer.pathX[0], false, actionIndex1);
							if (!bool_396_) {
								bool_396_ = calculatePath(2, 0, 1, 0, Client.localPlayer.pathY[0], 1, 0, actionIndex2,
										Client.localPlayer.pathX[0], false, actionIndex1);
							}
							anInt939 = clickX;
							anInt940 = clickY;
							anInt942 = 2;
							anInt941 = 0;
							outBuffer.putOpcode(253);
							outBuffer.putLEShort(actionIndex1 + regionAbsoluteBaseX);
							outBuffer.putLEShortA(actionIndex2 + regionAbsoluteBaseY);
							outBuffer.putShortA(actionIndex3);
						}
						if (menuActionIndex == 1448) {
							ItemDefinition itemdefinition = ItemDefinition.getDefinition(actionIndex3);
							String string;
							if (itemdefinition.description != null) {
								string = new String(itemdefinition.description);
							} else {
								string = "It's a " + itemdefinition.name + ".";
							}
							sendMessage(string, 0, "");
						}
						anInt1307 = 0;
						anInt1161 = 0;
						drawTabArea = true;
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("53, " + pressedRow + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method70(int i) {
		do {
			try {
				anInt1276 = 0;
				int i_397_ = (Client.localPlayer.xWithBoundary >> 7) + regionAbsoluteBaseX;
				int i_398_ = (Client.localPlayer.yWithBoundary >> 7) + regionAbsoluteBaseY;
				i = 58 / i;
				if (i_397_ >= 3053 && i_397_ <= 3156 && i_398_ >= 3056 && i_398_ <= 3136) {
					anInt1276 = 1;
				}
				if (i_397_ >= 3072 && i_397_ <= 3118 && i_398_ >= 9492 && i_398_ <= 9535) {
					anInt1276 = 1;
				}
				if (anInt1276 != 1 || i_397_ < 3139 || i_397_ > 3199 || i_398_ < 3008 || i_398_ > 3062) {
					break;
				}
				anInt1276 = 0;
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("12723, " + i + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	@Override
	public final void run() {
		if (aBoolean905) {
			method136((byte) 59);
		} else {
			super.run();
		}
	}

	public final void processClickingGame() {
		do {
			try {
				if (anInt1307 == 0 && anInt1161 == 0) {
					menuActionNames[menuActionRow] = "Walk here";
					menuActionIndexes[menuActionRow] = 516;
					menuActionIndex2[menuActionRow] = mouseEventX;
					menuActionIndex3[menuActionRow] = mouseEventY;
					menuActionRow++;
				}
				int i_399_ = -1;
				for (int i_400_ = 0; i_400_ < Model.anInt1680; i_400_++) {
					int i_401_ = Model.anIntArray1681[i_400_];
					int i_402_ = i_401_ & 0x7f;
					int i_403_ = i_401_ >> 7 & 0x7f;
					int i_404_ = i_401_ >> 29 & 0x3;
					int i_405_ = i_401_ >> 14 & 0x7fff;
					if (i_401_ != i_399_) {
						i_399_ = i_401_;
						if (i_404_ == 2 && currentScene.method526(currentSceneIndex, i_402_, i_403_, i_401_) >= 0) {
							GameObjectDefinition gameobjectdefinition = GameObjectDefinition.getDefinition(i_405_);
							if (gameobjectdefinition.anIntArray264 != null) {
								gameobjectdefinition = gameobjectdefinition.unknown2();
							}
							if (gameobjectdefinition == null) {
								continue;
							}
							if (anInt1307 == 1) {
								menuActionNames[menuActionRow] = "Use " + aString1311 + " with @cya@"
										+ gameobjectdefinition.name;
								menuActionIndexes[menuActionRow] = 62;
								menuActionIndex1[menuActionRow] = i_401_;
								menuActionIndex2[menuActionRow] = i_402_;
								menuActionIndex3[menuActionRow] = i_403_;
								menuActionRow++;
							} else if (anInt1161 == 1) {
								if ((anInt1163 & 0x4) == 4) {
									menuActionNames[menuActionRow] = aString1164 + " @cya@" + gameobjectdefinition.name;
									menuActionIndexes[menuActionRow] = 956;
									menuActionIndex1[menuActionRow] = i_401_;
									menuActionIndex2[menuActionRow] = i_402_;
									menuActionIndex3[menuActionRow] = i_403_;
									menuActionRow++;
								}
							} else {
								if (gameobjectdefinition.actions != null) {
									for (int i_406_ = 4; i_406_ >= 0; i_406_--) {
										if (gameobjectdefinition.actions[i_406_] != null) {
											menuActionNames[menuActionRow] = gameobjectdefinition.actions[i_406_]
													+ " @cya@" + gameobjectdefinition.name;
											if (i_406_ == 0) {
												menuActionIndexes[menuActionRow] = 502;
											}
											if (i_406_ == 1) {
												menuActionIndexes[menuActionRow] = 900;
											}
											if (i_406_ == 2) {
												menuActionIndexes[menuActionRow] = 113;
											}
											if (i_406_ == 3) {
												menuActionIndexes[menuActionRow] = 872;
											}
											if (i_406_ == 4) {
												menuActionIndexes[menuActionRow] = 1062;
											}
											menuActionIndex1[menuActionRow] = i_401_;
											menuActionIndex2[menuActionRow] = i_402_;
											menuActionIndex3[menuActionRow] = i_403_;
											menuActionRow++;
										}
									}
								}
								menuActionNames[menuActionRow] = "Examine @cya@" + gameobjectdefinition.name;
								menuActionIndexes[menuActionRow] = 1226;
								menuActionIndex1[menuActionRow] = gameobjectdefinition.id << 14;
								menuActionIndex2[menuActionRow] = i_402_;
								menuActionIndex3[menuActionRow] = i_403_;
								menuActionRow++;
							}
						}
						if (i_404_ == 1) {
							Npc npc = actors[i_405_];
							if (npc.npcDefinition.boundaryDimension == 1 && (npc.xWithBoundary & 0x7f) == 64
									&& (npc.yWithBoundary & 0x7f) == 64) {
								for (int i_407_ = 0; i_407_ < actorCount; i_407_++) {
									Npc npc_408_ = actors[anIntArray862[i_407_]];
									if (npc_408_ != null && npc_408_ != npc
											&& npc_408_.npcDefinition.boundaryDimension == 1
											&& npc_408_.xWithBoundary == npc.xWithBoundary
											&& npc_408_.yWithBoundary == npc.yWithBoundary) {
										method87(npc_408_.npcDefinition, anIntArray862[i_407_], false, i_403_, i_402_);
									}
								}
								for (int i_409_ = 0; i_409_ < playerCount; i_409_++) {
									Player player = players[anIntArray917[i_409_]];
									if (player != null && player.xWithBoundary == npc.xWithBoundary
											&& player.yWithBoundary == npc.yWithBoundary) {
										method88(i_402_, anIntArray917[i_409_], player, false, i_403_);
									}
								}
							}
							method87(npc.npcDefinition, i_405_, false, i_403_, i_402_);
						}
						if (i_404_ == 0) {
							Player player = players[i_405_];
							if ((player.xWithBoundary & 0x7f) == 64 && (player.yWithBoundary & 0x7f) == 64) {
								for (int i_410_ = 0; i_410_ < actorCount; i_410_++) {
									Npc npc = actors[anIntArray862[i_410_]];
									if (npc != null && npc.npcDefinition.boundaryDimension == 1
											&& npc.xWithBoundary == player.xWithBoundary
											&& npc.yWithBoundary == player.yWithBoundary) {
										method87(npc.npcDefinition, anIntArray862[i_410_], false, i_403_, i_402_);
									}
								}
								for (int i_411_ = 0; i_411_ < playerCount; i_411_++) {
									Player player_412_ = players[anIntArray917[i_411_]];
									if (player_412_ != null && player_412_ != player
											&& player_412_.xWithBoundary == player.xWithBoundary
											&& player_412_.yWithBoundary == player.yWithBoundary) {
										method88(i_402_, anIntArray917[i_411_], player_412_, false, i_403_);
									}
								}
							}
							method88(i_402_, i_405_, player, false, i_403_);
						}
						if (i_404_ == 3) {
							LinkedList linkedlist = groundItemNodes[currentSceneIndex][i_402_][i_403_];
							if (linkedlist != null) {
								for (Item item = (Item) linkedlist.getFront(); item != null; item = (Item) linkedlist
										.getNext()) {
									ItemDefinition itemdefinition = ItemDefinition.getDefinition(item.itemId);
									if (anInt1307 == 1) {
										menuActionNames[menuActionRow] = "Use " + aString1311 + " with @lre@"
												+ itemdefinition.name;
										menuActionIndexes[menuActionRow] = 511;
										menuActionIndex1[menuActionRow] = item.itemId;
										menuActionIndex2[menuActionRow] = i_402_;
										menuActionIndex3[menuActionRow] = i_403_;
										menuActionRow++;
									} else if (anInt1161 == 1) {
										if ((anInt1163 & 0x1) == 1) {
											menuActionNames[menuActionRow] = aString1164 + " @lre@"
													+ itemdefinition.name;
											menuActionIndexes[menuActionRow] = 94;
											menuActionIndex1[menuActionRow] = item.itemId;
											menuActionIndex2[menuActionRow] = i_402_;
											menuActionIndex3[menuActionRow] = i_403_;
											menuActionRow++;
										}
									} else {
										for (int i_413_ = 4; i_413_ >= 0; i_413_--) {
											if (itemdefinition.groundActions != null
													&& itemdefinition.groundActions[i_413_] != null) {
												menuActionNames[menuActionRow] = itemdefinition.groundActions[i_413_]
														+ " @lre@" + itemdefinition.name;
												if (i_413_ == 0) {
													menuActionIndexes[menuActionRow] = 652;
												}
												if (i_413_ == 1) {
													menuActionIndexes[menuActionRow] = 567;
												}
												if (i_413_ == 2) {
													menuActionIndexes[menuActionRow] = 234;
												}
												if (i_413_ == 3) {
													menuActionIndexes[menuActionRow] = 244;
												}
												if (i_413_ == 4) {
													menuActionIndexes[menuActionRow] = 213;
												}
												menuActionIndex1[menuActionRow] = item.itemId;
												menuActionIndex2[menuActionRow] = i_402_;
												menuActionIndex3[menuActionRow] = i_403_;
												menuActionRow++;
											} else if (i_413_ == 2) {
												menuActionNames[menuActionRow] = "Take @lre@" + itemdefinition.name;
												menuActionIndexes[menuActionRow] = 234;
												menuActionIndex1[menuActionRow] = item.itemId;
												menuActionIndex2[menuActionRow] = i_402_;
												menuActionIndex3[menuActionRow] = i_403_;
												menuActionRow++;
											}
										}
										menuActionNames[menuActionRow] = "Examine @lre@" + itemdefinition.name;
										menuActionIndexes[menuActionRow] = 1448;
										menuActionIndex1[menuActionRow] = item.itemId;
										menuActionIndex2[menuActionRow] = i_402_;
										menuActionIndex3[menuActionRow] = i_403_;
										menuActionRow++;
									}
								}
							}
						}
					}
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("26026, " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	@Override
	public final void shutdown() {
		SignLink.accessible = false;
		try {
			if (bufferedConnection != null) {
				bufferedConnection.close();
			}
		} catch (Exception exception) {
			/* empty */
		}
		bufferedConnection = null;
		stopMusic();
		if (mouseCapturer != null) {
			mouseCapturer.capturing = false;
		}
		mouseCapturer = null;
		onDemandRequester.stop();
		onDemandRequester = null;
		aBuffer859 = null;
		outBuffer = null;
		aBuffer872 = null;
		inBuffer = null;
		anIntArray1259 = null;
		aByteArrayArray1208 = null;
		aByteArrayArray1272 = null;
		anIntArray1260 = null;
		anIntArray1261 = null;
		anIntArrayArrayArray1239 = null;
		currentSceneTileFlags = null;
		currentScene = null;
		currentCollisionMap = null;
		wayPoints = null;
		distanceValues = null;
		walkingQueueX = null;
		walkingQueueY = null;
		aByteArray937 = null;
		aProducingGraphicsBuffer1188 = null;
		aProducingGraphicsBuffer1189 = null;
		currentSceneBuffer = null;
		aProducingGraphicsBuffer1191 = null;
		aProducingGraphicsBuffer1148 = null;
		aProducingGraphicsBuffer1149 = null;
		aProducingGraphicsBuffer1150 = null;
		aProducingGraphicsBuffer928 = null;
		aProducingGraphicsBuffer929 = null;
		aProducingGraphicsBuffer930 = null;
		aProducingGraphicsBuffer931 = null;
		aProducingGraphicsBuffer932 = null;
		aProducingGraphicsBuffer933 = null;
		aProducingGraphicsBuffer934 = null;
		aProducingGraphicsBuffer935 = null;
		aProducingGraphicsBuffer936 = null;
		inventoryBackgroundImage = null;
		minimapBackgroundImage = null;
		chatboxBackgroundImage = null;
		anIndexedImage1052 = null;
		anIndexedImage1053 = null;
		anIndexedImage1054 = null;
		anIndexedImageArray972 = null;
		anIndexedImage1168 = null;
		anIndexedImage1169 = null;
		anIndexedImage1170 = null;
		anIndexedImage1171 = null;
		anIndexedImage1172 = null;
		anIndexedImage890 = null;
		anIndexedImage891 = null;
		anIndexedImage892 = null;
		anIndexedImage893 = null;
		anIndexedImage894 = null;
		minimapCompass = null;
		anImageRGBArray1012 = null;
		anImageRGBArray1120 = null;
		anImageRGBArray1175 = null;
		mapdotItem = null;
		mapdotActor = null;
		mapdotPlayer = null;
		mapdotFriend = null;
		mapdotTeammate = null;
		anIndexedImageArray1085 = null;
		worldMapHintIcons = null;
		anIntArrayArray954 = null;
		players = null;
		anIntArray917 = null;
		anIntArray919 = null;
		playerBuffer = null;
		anIntArray865 = null;
		actors = null;
		anIntArray862 = null;
		groundItemNodes = null;
		aLinkedList1204 = null;
		projectileList = null;
		aLinkedList1081 = null;
		menuActionIndex2 = null;
		menuActionIndex3 = null;
		menuActionIndexes = null;
		menuActionIndex1 = null;
		menuActionNames = null;
		settings = null;
		minimapHintX = null;
		minimapHintY = null;
		minimapHint = null;
		minimapImage = null;
		friendsListNames = null;
		friendsListLongs = null;
		friendsListWorlds = null;
		flameLeftBackground = null;
		flameRightBackground = null;
		aProducingGraphicsBuffer1132 = null;
		aProducingGraphicsBuffer1133 = null;
		aProducingGraphicsBuffer1134 = null;
		aProducingGraphicsBuffer1137 = null;
		aProducingGraphicsBuffer1138 = null;
		aProducingGraphicsBuffer1139 = null;
		aProducingGraphicsBuffer1140 = null;
		method118();
		GameObjectDefinition.reset();
		ActorDefinition.reset();
		ItemDefinition.reset();
		FloorDefinition.cache = null;
		IdentityKit.cache = null;
		Widget.cache = null;
		AnimationSequence.cache = null;
		SpotAnimation.cache = null;
		SpotAnimation.modelCache = null;
		Varp.aVarpArray746 = null;
		producingGraphicsBuffer = null;
		Player.modelCache = null;
		Rasterizer3D.reset();
		Scene.method495(-501);
		Model.reset();
		Animation.reset();
		System.gc();
	}

	public void method72() {
		System.out.println("============");
		System.out.println("flame-cycle:" + anInt1233);
		if (onDemandRequester != null) {
			System.out.println("Od-cycle:" + onDemandRequester.cycle);
		}
		System.out.println("loop-cycle:" + Client.currentCycle);
		System.out.println("draw-cycle:" + Client.anInt1086);
		System.out.println("ptype:" + opcode);
		System.out.println("psize:" + packetSize);
		if (bufferedConnection != null) {
			bufferedConnection.printDebug();
		}
		dumpRequested = true;
	}

	@Override
	public final Component getComponent() {
		if (SignLink.applet != null) {
			return SignLink.applet;
		}
		if (gameFrame != null) {
			return gameFrame;
		}
		return this;
	}

	public final void method73(int i) {
		try {
			i = 55 / i;
			for (;;) {
				int key = this.readCharacter();
				if (key == -1) {
					break;
				}
				if (openWidgetIndex != -1 && openWidgetIndex == anInt1203) {
					if (key == 8 && reportWidgetName.length() > 0) {
						reportWidgetName = reportWidgetName.substring(0, reportWidgetName.length() - 1);
					}
					if ((key >= 97 && key <= 122 || key >= 65 && key <= 90 || key >= 48 && key <= 57 || key == 32)
							&& reportWidgetName.length() < 12) {
						reportWidgetName += (char) key;
					}
				} else if (aBoolean1281) {
					if (key >= 32 && key <= 122 && chatMessage.length() < 80) {
						chatMessage += (char) key;
						aBoolean1248 = true;
					}
					if (key == 8 && chatMessage.length() > 0) {
						chatMessage = chatMessage.substring(0, chatMessage.length() - 1);
						aBoolean1248 = true;
					}
					if (key == 13 || key == 10) {
						aBoolean1281 = false;
						aBoolean1248 = true;
						if (anInt1089 == 1) {
							long l = TextUtils.nameToLong(chatMessage);
							method41((byte) 68, l);
						}
						if (anInt1089 == 2 && friendsListCount > 0) {
							long l = TextUtils.nameToLong(chatMessage);
							method35(false, l);
						}
						if (anInt1089 == 3 && chatMessage.length() > 0) {
							outBuffer.putOpcode(126);
							outBuffer.put(0);
							int i_415_ = outBuffer.offset;
							outBuffer.putLong(aLong978);
							ChatEncoder.put(chatMessage, outBuffer);
							outBuffer.putSizeByte(outBuffer.offset - i_415_);
							chatMessage = ChatEncoder.formatChatMessage(chatMessage);
							chatMessage = ChatCensor.censorString(chatMessage);
							sendMessage(chatMessage, 6, TextUtils.formatName(TextUtils.longToName(aLong978)));
							if (privateChatSetting == 2) {
								privateChatSetting = 1;
								aBoolean1258 = true;
								outBuffer.putOpcode(95);
								outBuffer.put(publicChatSetting);
								outBuffer.put(privateChatSetting);
								outBuffer.put(tradeSetting);
							}
						}
						if (anInt1089 == 4 && ignoreListCount < 100) {
							long l = TextUtils.nameToLong(chatMessage);
							method113(l, 4);
						}
						if (anInt1089 == 5 && ignoreListCount > 0) {
							long l = TextUtils.nameToLong(chatMessage);
							method122(3, l);
						}
					}
				} else if (inputType == 1) {
					if (key >= KeyEvent.VK_0 && key <= KeyEvent.VK_9 && inputInputMessage.length() < 10) {
						inputInputMessage += (char) key;
						aBoolean1248 = true;
					}
					if (key == KeyEvent.VK_BACK_SPACE && inputInputMessage.length() > 0) {
						inputInputMessage = inputInputMessage.substring(0, inputInputMessage.length() - 1);
						aBoolean1248 = true;
					}
					if (key == KeyEvent.VK_ENTER) {
						if (inputInputMessage.length() > 0) {
							int inputValue = 0;
							try {
								inputValue = Integer.parseInt(inputInputMessage);
							} catch (Exception e) {
								/* empty */
							}
							outBuffer.putOpcode(208);
							outBuffer.putInt(inputValue);
						}
						inputType = 0;
						aBoolean1248 = true;
					}
				} else if (inputType == 2) {
					if (key >= 32 && key <= 122 && inputInputMessage.length() < 12) {
						inputInputMessage += (char) key;
						aBoolean1248 = true;
					}
					if (key == KeyEvent.VK_BACK_SPACE && inputInputMessage.length() > 0) {
						inputInputMessage = inputInputMessage.substring(0, inputInputMessage.length() - 1);
						aBoolean1248 = true;
					}
					if (key == KeyEvent.VK_ENTER) {
						if (inputInputMessage.length() > 0) {
							outBuffer.putOpcode(60);
							outBuffer.putLong(TextUtils.nameToLong(inputInputMessage));
						}
						inputType = 0;
						aBoolean1248 = true;
					}
				} else if (chatboxWidgetIndex == -1) {
					if (key >= 32 && key <= 122 && chatboxInput.length() < 80) {
						System.out.println((char) 32);
						chatboxInput += (char) key;
						aBoolean1248 = true;
					}
					if (key == KeyEvent.VK_BACK_SPACE && chatboxInput.length() > 0) {
						chatboxInput = chatboxInput.substring(0, chatboxInput.length() - 1);
						aBoolean1248 = true;
					}
					if ((key == KeyEvent.VK_ENTER) && chatboxInput.length() > 0) {
						if (playerRights == 0) {
							if (chatboxInput.equals("::clientdrop")) {
								method68(-670);
							}
							if (chatboxInput.equals("::lag")) {
								method72();
							}
							if (chatboxInput.equals("::prefetchmusic")) {
								for (int i_417_ = 0; i_417_ < onDemandRequester.fileCount(2); i_417_++) {
									onDemandRequester.setPriority((byte) 1, 2, i_417_);
								}
							}
							if (chatboxInput.equals("::fps")) {
								Client.debug = !Client.debug;
							}
							if (chatboxInput.equals("::noclip")) {
								for (int plane = 0; plane < 4; plane++) {
									for (int x = 1; x < 103; x++) {
										for (int y = 1; y < 103; y++) {
											currentCollisionMap[plane].adjacency[x][y] = 0;
										}
									}
								}
							}
						}
						if (chatboxInput.startsWith("::")) {
							outBuffer.putOpcode(103);
							outBuffer.put(chatboxInput.length() - 1);
							outBuffer.putString(chatboxInput.substring(2));
						} else {
							String string = chatboxInput.toLowerCase();
							int i_421_ = 0;
							if (string.startsWith("yellow:")) {
								i_421_ = 0;
								chatboxInput = chatboxInput.substring(7);
							} else if (string.startsWith("red:")) {
								i_421_ = 1;
								chatboxInput = chatboxInput.substring(4);
							} else if (string.startsWith("green:")) {
								i_421_ = 2;
								chatboxInput = chatboxInput.substring(6);
							} else if (string.startsWith("cyan:")) {
								i_421_ = 3;
								chatboxInput = chatboxInput.substring(5);
							} else if (string.startsWith("purple:")) {
								i_421_ = 4;
								chatboxInput = chatboxInput.substring(7);
							} else if (string.startsWith("white:")) {
								i_421_ = 5;
								chatboxInput = chatboxInput.substring(6);
							} else if (string.startsWith("flash1:")) {
								i_421_ = 6;
								chatboxInput = chatboxInput.substring(7);
							} else if (string.startsWith("flash2:")) {
								i_421_ = 7;
								chatboxInput = chatboxInput.substring(7);
							} else if (string.startsWith("flash3:")) {
								i_421_ = 8;
								chatboxInput = chatboxInput.substring(7);
							} else if (string.startsWith("glow1:")) {
								i_421_ = 9;
								chatboxInput = chatboxInput.substring(6);
							} else if (string.startsWith("glow2:")) {
								i_421_ = 10;
								chatboxInput = chatboxInput.substring(6);
							} else if (string.startsWith("glow3:")) {
								i_421_ = 11;
								chatboxInput = chatboxInput.substring(6);
							}
							string = chatboxInput.toLowerCase();
							int i_422_ = 0;
							if (string.startsWith("wave:")) {
								i_422_ = 1;
								chatboxInput = chatboxInput.substring(5);
							} else if (string.startsWith("wave2:")) {
								i_422_ = 2;
								chatboxInput = chatboxInput.substring(6);
							} else if (string.startsWith("shake:")) {
								i_422_ = 3;
								chatboxInput = chatboxInput.substring(6);
							} else if (string.startsWith("scroll:")) {
								i_422_ = 4;
								chatboxInput = chatboxInput.substring(7);
							} else if (string.startsWith("slide:")) {
								i_422_ = 5;
								chatboxInput = chatboxInput.substring(6);
							}
							outBuffer.putOpcode(4);
							outBuffer.put(0);
							int i_423_ = outBuffer.offset;
							outBuffer.putByteS(i_422_);
							outBuffer.putByteS(i_421_);
							aBuffer859.offset = 0;
							ChatEncoder.put(chatboxInput, aBuffer859);
							outBuffer.putBytesA(0, aBuffer859.payload, aBuffer859.offset);
							outBuffer.putSizeByte(outBuffer.offset - i_423_);
							chatboxInput = ChatEncoder.formatChatMessage(chatboxInput);
							chatboxInput = ChatCensor.censorString(chatboxInput);
							Client.localPlayer.forcedChat = chatboxInput;
							Client.localPlayer.chatColor = i_421_;
							Client.localPlayer.chatEffect = i_422_;
							Client.localPlayer.anInt1555 = 150;
							if (playerRights == 2) {
								sendMessage(Client.localPlayer.forcedChat, 2, "@cr2@" + Client.localPlayer.playerName);
							} else if (playerRights == 1) {
								sendMessage(Client.localPlayer.forcedChat, 2, "@cr1@" + Client.localPlayer.playerName);
							} else {
								sendMessage(Client.localPlayer.forcedChat, 2, Client.localPlayer.playerName);
							}
							if (publicChatSetting == 2) {
								publicChatSetting = 3;
								aBoolean1258 = true;
								outBuffer.putOpcode(95);
								outBuffer.put(publicChatSetting);
								outBuffer.put(privateChatSetting);
								outBuffer.put(tradeSetting);
							}
						}
						chatboxInput = "";
						aBoolean1248 = true;
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("88130, " + i + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method74(int i, int i_424_) {
		try {
			int i_426_ = 0;
			for (int i_427_ = 0; i_427_ < 100; i_427_++) {
				if (chatboxMessage[i_427_] != null) {
					int i_428_ = chatboxMessageType[i_427_];
					int i_429_ = 70 - i_426_ * 14 + anInt1114 + 4;
					if (i_429_ < -20) {
						break;
					}
					String name = chatboxMessageName[i_427_];
					if (name != null && name.startsWith("@cr1@")) {
						name = name.substring(5);
					}
					if (name != null && name.startsWith("@cr2@")) {
						name = name.substring(5);
					}
					if (i_428_ == 0) {
						i_426_++;
					}
					if ((i_428_ == 1 || i_428_ == 2)
							&& (i_428_ == 1 || publicChatSetting == 0 || publicChatSetting == 1
									&& method109(false, name))) {
						if (i_424_ > i_429_ - 14 && i_424_ <= i_429_ && !name.equals(Client.localPlayer.playerName)) {
							if (playerRights >= 1) {
								menuActionNames[menuActionRow] = "Report abuse @whi@" + name;
								menuActionIndexes[menuActionRow] = 606;
								menuActionRow++;
							}
							menuActionNames[menuActionRow] = "Add ignore @whi@" + name;
							menuActionIndexes[menuActionRow] = 42;
							menuActionRow++;
							menuActionNames[menuActionRow] = "Add friend @whi@" + name;
							menuActionIndexes[menuActionRow] = 337;
							menuActionRow++;
						}
						i_426_++;
					}
					if ((i_428_ == 3 || i_428_ == 7)
							&& anInt1220 == 0
							&& (i_428_ == 7 || privateChatSetting == 0 || privateChatSetting == 1
									&& method109(false, name))) {
						if (i_424_ > i_429_ - 14 && i_424_ <= i_429_) {
							if (playerRights >= 1) {
								menuActionNames[menuActionRow] = "Report abuse @whi@" + name;
								menuActionIndexes[menuActionRow] = 606;
								menuActionRow++;
							}
							menuActionNames[menuActionRow] = "Add ignore @whi@" + name;
							menuActionIndexes[menuActionRow] = 42;
							menuActionRow++;
							menuActionNames[menuActionRow] = "Add friend @whi@" + name;
							menuActionIndexes[menuActionRow] = 337;
							menuActionRow++;
						}
						i_426_++;
					}
					if (i_428_ == 4 && (tradeSetting == 0 || tradeSetting == 1 && method109(false, name))) {
						if (i_424_ > i_429_ - 14 && i_424_ <= i_429_) {
							menuActionNames[menuActionRow] = "Accept trade @whi@" + name;
							menuActionIndexes[menuActionRow] = 484;
							menuActionRow++;
						}
						i_426_++;
					}
					if ((i_428_ == 5 || i_428_ == 6) && anInt1220 == 0 && privateChatSetting < 2) {
						i_426_++;
					}
					if (i_428_ == 8 && (tradeSetting == 0 || tradeSetting == 1 && method109(false, name))) {
						if (i_424_ > i_429_ - 14 && i_424_ <= i_429_) {
							menuActionNames[menuActionRow] = "Accept challenge @whi@" + name;
							menuActionIndexes[menuActionRow] = 6;
							menuActionRow++;
						}
						i_426_++;
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("86869, " + i + ", " + i_424_ + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method75(Widget widget) {
		do {
			try {
				int type = widget.contentType;
				if (type >= 1 && type <= 100 || type >= 701 && type <= 800) {
					if (type == 1 && friendListStatus == 0) {
						widget.disabledText = "Loading friend list";
						widget.actionType = 0;
					} else if (type == 1 && friendListStatus == 1) {
						widget.disabledText = "Connecting to friendserver";
						widget.actionType = 0;
					} else if (type == 2 && friendListStatus != 2) {
						widget.disabledText = "Please wait...";
						widget.actionType = 0;
					} else {
						int friendCount = friendsListCount;
						if (friendListStatus != 2) {
							friendCount = 0;
						}
						if (type > 700) {
							type -= 601;
						} else {
							type--;
						}
						if (type >= friendCount) {
							widget.disabledText = "";
							widget.actionType = 0;
						} else {
							widget.disabledText = friendsListNames[type];
							widget.actionType = 1;
						}
					}
				} else if (type >= 101 && type <= 200 || type >= 801 && type <= 900) {
					int i_433_ = friendsListCount;
					if (friendListStatus != 2) {
						i_433_ = 0;
					}
					if (type > 800) {
						type -= 701;
					} else {
						type -= 101;
					}
					if (type >= i_433_) {
						widget.disabledText = "";
						widget.actionType = 0;
					} else {
						if (friendsListWorlds[type] == 0) {
							widget.disabledText = "@red@Offline";
						} else if (friendsListWorlds[type] == Client.nodeIndex) {
							widget.disabledText = "@gre@World-" + (friendsListWorlds[type] - 9);
						} else {
							widget.disabledText = "@yel@World-" + (friendsListWorlds[type] - 9);
						}
						widget.actionType = 1;
					}
				} else if (type == 203) {
					int i_434_ = friendsListCount;
					if (friendListStatus != 2) {
						i_434_ = 0;
					}
					widget.scrollLimit = i_434_ * 15 + 20;
					if (widget.scrollLimit <= widget.height) {
						widget.scrollLimit = widget.height + 1;
					}
				} else if (type >= 401 && type <= 500) {
					type -= 401;
					if (type == 0 && friendListStatus == 0) {
						widget.disabledText = "Loading ignore list";
						widget.actionType = 0;
					} else if (type == 1 && friendListStatus == 0) {
						widget.disabledText = "Please wait...";
						widget.actionType = 0;
					} else {
						int i_435_ = ignoreListCount;
						if (friendListStatus == 0) {
							i_435_ = 0;
						}
						if (type >= i_435_) {
							widget.disabledText = "";
							widget.actionType = 0;
						} else {
							widget.disabledText = TextUtils.formatName(TextUtils.longToName(ignoreList[type]));
							widget.actionType = 1;
						}
					}
				} else if (type == 503) {
					widget.scrollLimit = ignoreListCount * 15 + 20;
					if (widget.scrollLimit <= widget.height) {
						widget.scrollLimit = widget.height + 1;
					}
				} else if (type == 327) {
					widget.rotationX = 150;
					widget.rotationY = (int) (Math.sin(Client.currentCycle / 40.0) * 256.0) & 0x7ff;
					if (aBoolean1056) {
						for (int i_436_ = 0; i_436_ < 7; i_436_++) {
							int i_437_ = anIntArray1090[i_436_];
							if (i_437_ >= 0 && !IdentityKit.cache[i_437_].isBodyModelCached()) {
								return;
							}
						}
						aBoolean1056 = false;
						Model[] models = new Model[7];
						int i_438_ = 0;
						for (int i_439_ = 0; i_439_ < 7; i_439_++) {
							int i_440_ = anIntArray1090[i_439_];
							if (i_440_ >= 0) {
								models[i_438_++] = IdentityKit.cache[i_440_].getBodyModel();
							}
						}
						Model model = new Model(i_438_, models);
						for (int i_441_ = 0; i_441_ < 5; i_441_++) {
							if (anIntArray1015[i_441_] != 0) {
								model.recolor(Client.anIntArrayArray1028[i_441_][0],
										Client.anIntArrayArray1028[i_441_][anIntArray1015[i_441_]]);
								if (i_441_ == 1) {
									model.recolor(Client.anIntArray1229[0],
											Client.anIntArray1229[anIntArray1015[i_441_]]);
								}
							}
						}
						model.createBones();
						model.applyTransform(AnimationSequence.cache[Client.localPlayer.standAnimationId].frame2Ids[0]);
						model.applyLighting(64, 850, -30, -50, -30, true);
						widget.modelType = 5;
						widget.modelIndex = 0;
						Widget.setModel(0, 5, model);
					}
				} else if (type == 324) {
					if (anImageRGB956 == null) {
						anImageRGB956 = widget.disabledImage;
						anImageRGB957 = widget.enabledImage;
					}
					if (aBoolean1072) {
						widget.disabledImage = anImageRGB957;
					} else {
						widget.disabledImage = anImageRGB956;
					}
				} else if (type == 325) {
					if (anImageRGB956 == null) {
						anImageRGB956 = widget.disabledImage;
						anImageRGB957 = widget.enabledImage;
					}
					if (aBoolean1072) {
						widget.disabledImage = anImageRGB956;
					} else {
						widget.disabledImage = anImageRGB957;
					}
				} else if (type == 600) {
					widget.disabledText = reportWidgetName;
					if (Client.currentCycle % 20 < 10) {
						widget.disabledText += "|";
					} else {
						widget.disabledText += " ";
					}
				} else {
					if (type == 613) {
						if (playerRights >= 1) {
							if (aBoolean1183) {
								widget.disabledColor = 16711680;
								widget.disabledText = "Moderator option: Mute player for 48 hours: <ON>";
							} else {
								widget.disabledColor = 0xFFFFFF;
								widget.disabledText = "Moderator option: Mute player for 48 hours: <OFF>";
							}
						} else {
							widget.disabledText = "";
						}
					}
					if (type == 650 || type == 655) {
						if (lastAddress != 0) {
							String string;
							if (lastLogin == 0) {
								string = "earlier today";
							} else if (lastLogin == 1) {
								string = "yesterday";
							} else {
								string = String.valueOf(lastLogin) + " days ago";
							}
							widget.disabledText = "You last logged in " + string + " from: " + SignLink.lastHostname;
						} else {
							widget.disabledText = "";
						}
					}
					if (type == 651) {
						if (unreadMessages == 0) {
							widget.disabledText = "0 unread messages";
							widget.disabledColor = 0xFFFF00;
						}
						if (unreadMessages == 1) {
							widget.disabledText = "1 unread message";
							widget.disabledColor = 65280;
						}
						if (unreadMessages > 1) {
							widget.disabledText = String.valueOf(unreadMessages) + " unread messages";
							widget.disabledColor = 65280;
						}
					}
					if (type == 652) {
						if (lastRecoveryChange == 201) {
							if (membershipAdviser == 1) {
								widget.disabledText = "@yel@This is a non-members world: @whi@Since you are a member we";
							} else {
								widget.disabledText = "";
							}
						} else if (lastRecoveryChange == 200) {
							widget.disabledText = "You have not yet set any password recovery questions.";
						} else {
							String string;
							if (lastRecoveryChange == 0) {
								string = "Earlier today";
							} else if (lastRecoveryChange == 1) {
								string = "Yesterday";
							} else {
								string = String.valueOf(lastRecoveryChange) + " days ago";
							}
							widget.disabledText = string + " you changed your recovery questions";
						}
					}
					if (type == 653) {
						if (lastRecoveryChange == 201) {
							if (membershipAdviser == 1) {
								widget.disabledText = "@whi@recommend you use a members world instead. You may use";
							} else {
								widget.disabledText = "";
							}
						} else if (lastRecoveryChange == 200) {
							widget.disabledText = "We strongly recommend you do so now to secure your account.";
						} else {
							widget.disabledText = "If you do not remember making this change then cancel it immediately";
						}
					}
					if (type != 654) {
						break;
					}
					if (lastRecoveryChange == 201) {
						if (membershipAdviser == 1) {
							widget.disabledText = "@whi@this world but member benefits are unavailable whilst here.";
						} else {
							widget.disabledText = "";
						}
					} else if (lastRecoveryChange == 200) {
						widget.disabledText = "Do this from the 'account management' area on our front webpage";
					} else {
						widget.disabledText = "Do this from the 'account management' area on our front webpage";
					}
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("36496, " + widget + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void method76(byte b) {
		try {
			if (anInt1220 != 0) {
				TypeFace typeface = fontNormal;
				if (b != aByte1299) {
					Client.aBoolean1256 = !Client.aBoolean1256;
				}
				int i = 0;
				if (systemUpdateTime != 0) {
					i = 1;
				}
				for (int i_442_ = 0; i_442_ < 100; i_442_++) {
					if (chatboxMessage[i_442_] != null) {
						int i_443_ = chatboxMessageType[i_442_];
						String string = chatboxMessageName[i_442_];
						int i_444_ = 0;
						if (string != null && string.startsWith("@cr1@")) {
							string = string.substring(5);
							i_444_ = 1;
						}
						if (string != null && string.startsWith("@cr2@")) {
							string = string.substring(5);
							i_444_ = 2;
						}
						if ((i_443_ == 3 || i_443_ == 7)
								&& (i_443_ == 7 || privateChatSetting == 0 || privateChatSetting == 1
										&& method109(false, string))) {
							int i_445_ = 329 - i * 13;
							int i_446_ = 4;
							typeface.drawRegularString("From", i_446_, i_445_, 0);
							typeface.drawRegularString("From", i_446_, i_445_ - 1, 65535);
							i_446_ += typeface.getStringEffectWidth("From ");
							if (i_444_ == 1) {
								anIndexedImageArray1244[0].drawImage(i_446_, i_445_ - 12);
								i_446_ += 14;
							}
							if (i_444_ == 2) {
								anIndexedImageArray1244[1].drawImage(i_446_, i_445_ - 12);
								i_446_ += 14;
							}
							typeface.drawRegularString(string + ": " + chatboxMessage[i_442_], i_446_, i_445_, 0);
							typeface.drawRegularString(string + ": " + chatboxMessage[i_442_], i_446_, i_445_ - 1,
									65535);
							if (++i >= 5) {
								break;
							}
						}
						if (i_443_ == 5 && privateChatSetting < 2) {
							int i_447_ = 329 - i * 13;
							typeface.drawRegularString(chatboxMessage[i_442_], 4, i_447_, 0);
							typeface.drawRegularString(chatboxMessage[i_442_], 4, i_447_ - 1, 65535);
							if (++i >= 5) {
								break;
							}
						}
						if (i_443_ == 6 && privateChatSetting < 2) {
							int i_448_ = 329 - i * 13;
							typeface.drawRegularString("To " + string + ": " + chatboxMessage[i_442_], 4, i_448_, 0);
							typeface.drawRegularString("To " + string + ": " + chatboxMessage[i_442_], 4, i_448_ - 1,
									65535);
							if (++i >= 5) {
								break;
							}
						}
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("85217, " + b + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void sendMessage(String message, int type, String prefix) {
		try {
			if (type == 0 && anInt1067 != -1) {
				aString869 = message;
				clickType = 0;
			}
			if (chatboxWidgetIndex == -1) {
				aBoolean1248 = true;
			}
			for (int i_450_ = 99; i_450_ > 0; i_450_--) {
				chatboxMessageType[i_450_] = chatboxMessageType[i_450_ - 1];
				chatboxMessageName[i_450_] = chatboxMessageName[i_450_ - 1];
				chatboxMessage[i_450_] = chatboxMessage[i_450_ - 1];
			}
			chatboxMessageType[0] = type;
			chatboxMessageName[0] = prefix;
			chatboxMessage[0] = message;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("56346, " + message + ", " + type + ", " + prefix + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void processClickingTabs() {
		try {
			if (clickType != 1) {
				return;
			}
			if (clickX >= 539 && clickX <= 573 && clickY >= 169 && clickY < 205 && tabWidgetIndexes[0] != -1) {
				drawTabArea = true;
				currentTabIndex = 0;
				aBoolean1128 = true;
			}
			if (clickX >= 569 && clickX <= 599 && clickY >= 168 && clickY < 205 && tabWidgetIndexes[1] != -1) {
				drawTabArea = true;
				currentTabIndex = 1;
				aBoolean1128 = true;
			}
			if (clickX >= 597 && clickX <= 627 && clickY >= 168 && clickY < 205 && tabWidgetIndexes[2] != -1) {
				drawTabArea = true;
				currentTabIndex = 2;
				aBoolean1128 = true;
			}
			if (clickX >= 625 && clickX <= 669 && clickY >= 168 && clickY < 203 && tabWidgetIndexes[3] != -1) {
				drawTabArea = true;
				currentTabIndex = 3;
				aBoolean1128 = true;
			}
			if (clickX >= 666 && clickX <= 696 && clickY >= 168 && clickY < 205 && tabWidgetIndexes[4] != -1) {
				drawTabArea = true;
				currentTabIndex = 4;
				aBoolean1128 = true;
			}
			if (clickX >= 694 && clickX <= 724 && clickY >= 168 && clickY < 205 && tabWidgetIndexes[5] != -1) {
				drawTabArea = true;
				currentTabIndex = 5;
				aBoolean1128 = true;
			}
			if (clickX >= 722 && clickX <= 756 && clickY >= 169 && clickY < 205 && tabWidgetIndexes[6] != -1) {
				drawTabArea = true;
				currentTabIndex = 6;
				aBoolean1128 = true;
			}
			if (clickX >= 540 && clickX <= 574 && clickY >= 466 && clickY < 502 && tabWidgetIndexes[7] != -1) {
				drawTabArea = true;
				currentTabIndex = 7;
				aBoolean1128 = true;
			}
			if (clickX >= 572 && clickX <= 602 && clickY >= 466 && clickY < 503 && tabWidgetIndexes[8] != -1) {
				drawTabArea = true;
				currentTabIndex = 8;
				aBoolean1128 = true;
			}
			if (clickX >= 599 && clickX <= 629 && clickY >= 466 && clickY < 503 && tabWidgetIndexes[9] != -1) {
				drawTabArea = true;
				currentTabIndex = 9;
				aBoolean1128 = true;
			}
			if (clickX >= 627 && clickX <= 671 && clickY >= 467 && clickY < 502 && tabWidgetIndexes[10] != -1) {
				drawTabArea = true;
				currentTabIndex = 10;
				aBoolean1128 = true;
			}
			if (clickX >= 669 && clickX <= 699 && clickY >= 466 && clickY < 503 && tabWidgetIndexes[11] != -1) {
				drawTabArea = true;
				currentTabIndex = 11;
				aBoolean1128 = true;
			}
			if (clickX >= 696 && clickX <= 726 && clickY >= 466 && clickY < 503 && tabWidgetIndexes[12] != -1) {
				drawTabArea = true;
				currentTabIndex = 12;
				aBoolean1128 = true;
			}
			if (clickX < 724 || clickX > 758 || clickY < 466 || clickY >= 502 || tabWidgetIndexes[13] == -1) {
				return;
			}
			drawTabArea = true;
			currentTabIndex = 13;
			aBoolean1128 = true;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("30484, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method79(int i) {
		try {
			if (aProducingGraphicsBuffer1191 == null) {
				method118();
				producingGraphicsBuffer = null;
				aProducingGraphicsBuffer1132 = null;
				aProducingGraphicsBuffer1133 = null;
				aProducingGraphicsBuffer1134 = null;
				flameLeftBackground = null;
				flameRightBackground = null;
				aProducingGraphicsBuffer1137 = null;
				aProducingGraphicsBuffer1138 = null;
				aProducingGraphicsBuffer1139 = null;
				aProducingGraphicsBuffer1140 = null;
				aProducingGraphicsBuffer1191 = new ProducingGraphicsBuffer(479, 96, getComponent());
				aProducingGraphicsBuffer1189 = new ProducingGraphicsBuffer(172, 156, getComponent());
				Rasterizer.resetPixels();
				minimapBackgroundImage.drawImage(0, 0);
				aProducingGraphicsBuffer1188 = new ProducingGraphicsBuffer(190, 261, getComponent());
				currentSceneBuffer = new ProducingGraphicsBuffer(512, 334, getComponent());
				Rasterizer.resetPixels();
				aProducingGraphicsBuffer1148 = new ProducingGraphicsBuffer(496, 50, getComponent());
				if (i != 1) {
					startup();
				}
				aProducingGraphicsBuffer1149 = new ProducingGraphicsBuffer(269, 37, getComponent());
				aProducingGraphicsBuffer1150 = new ProducingGraphicsBuffer(249, 45, getComponent());
				drawGameframe = true;
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("35544, " + i + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final String getHost() {
		try {
			if (SignLink.applet != null) {
				return SignLink.applet.getDocumentBase().getHost().toLowerCase();
			}
			if (gameFrame != null) {
				return "runescape.com";
			}
			return super.getDocumentBase().getHost().toLowerCase();
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("82775, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method81(ImageRGB imagergb, int i, int i_451_, int i_452_) {
		try {
			int i_453_ = i_452_ * i_452_ + i_451_ * i_451_;
			if (i >= 0) {
				startup();
			}
			if (i_453_ > 4225 && i_453_ < 90000) {
				int i_454_ = anInt1210 + anInt1234 & 0x7ff;
				int i_455_ = Model.anIntArray1682[i_454_];
				int i_456_ = Model.anIntArray1683[i_454_];
				i_455_ = i_455_ * 256 / (anInt1195 + 256);
				i_456_ = i_456_ * 256 / (anInt1195 + 256);
				int i_457_ = i_451_ * i_455_ + i_452_ * i_456_ >> 16;
				int i_458_ = i_451_ * i_456_ - i_452_ * i_455_ >> 16;
				double d = Math.atan2(i_457_, i_458_);
				int i_459_ = (int) (Math.sin(d) * 63.0);
				int i_460_ = (int) (Math.cos(d) * 57.0);
				anImageRGB1026.method350(83 - i_460_ - 20, 15, 20, 15, 41960, 256, 20, d, 94 + i_459_ + 4 - 10);
			} else {
				method141(imagergb, i_452_, i_451_);
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("37468, " + imagergb + ", " + i + ", " + i_451_ + ", " + i_452_ + ", "
					+ runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void processClickingAreas() {
		try {
			if (anInt1111 == 0) {
				menuActionNames[0] = "Cancel";
				menuActionIndexes[0] = 1107;
				menuActionRow = 1;
				processClickingPrivateMessages();

				anInt911 = 0;
				if (mouseEventX > 4 && mouseEventY > 4 && mouseEventX < 516 && mouseEventY < 338) {
					if (openWidgetIndex != -1) {
						processClickingWidget(4, Widget.cache[openWidgetIndex], mouseEventX, 4, mouseEventY, 0);
					} else {
						processClickingGame();
					}
				}
				if (anInt911 != anInt1051) {
					anInt1051 = anInt911;
				}

				anInt911 = 0;
				if (mouseEventX > 553 && mouseEventY > 205 && mouseEventX < 743 && mouseEventY < 466) {
					if (anInt1214 != -1) {
						processClickingWidget(553, Widget.cache[anInt1214], mouseEventX, 205, mouseEventY, 0);
					} else if (tabWidgetIndexes[currentTabIndex] != -1) {
						processClickingWidget(553, Widget.cache[tabWidgetIndexes[currentTabIndex]], mouseEventX, 205,
								mouseEventY, 0);
					}
				}
				if (anInt911 != anInt1073) {
					drawTabArea = true;
					anInt1073 = anInt911;
				}

				anInt911 = 0;
				if (mouseEventX > 17 && mouseEventY > 357 && mouseEventX < 496 && mouseEventY < 453) {
					if (chatboxWidgetIndex != -1) {
						processClickingWidget(17, Widget.cache[chatboxWidgetIndex], mouseEventX, 357, mouseEventY, 0);
					} else if (mouseEventY < 434 && mouseEventX < 426) {
						method74(mouseEventX - 17, mouseEventY - 357);
					}
				}
				if (chatboxWidgetIndex != -1 && anInt911 != anInt1064) {
					aBoolean1248 = true;
					anInt1064 = anInt911;
				}

				boolean bool = false;
				while (!bool) {
					bool = true;
					for (int actionIndex = 0; actionIndex < menuActionRow - 1; actionIndex++) {
						if (menuActionIndexes[actionIndex] < 1000 && menuActionIndexes[actionIndex + 1] > 1000) {
							String string = menuActionNames[actionIndex];
							menuActionNames[actionIndex] = menuActionNames[actionIndex + 1];
							menuActionNames[actionIndex + 1] = string;
							int i_462_ = menuActionIndexes[actionIndex];
							menuActionIndexes[actionIndex] = menuActionIndexes[actionIndex + 1];
							menuActionIndexes[actionIndex + 1] = i_462_;
							i_462_ = menuActionIndex2[actionIndex];
							menuActionIndex2[actionIndex] = menuActionIndex2[actionIndex + 1];
							menuActionIndex2[actionIndex + 1] = i_462_;
							i_462_ = menuActionIndex3[actionIndex];
							menuActionIndex3[actionIndex] = menuActionIndex3[actionIndex + 1];
							menuActionIndex3[actionIndex + 1] = i_462_;
							i_462_ = menuActionIndex1[actionIndex];
							menuActionIndex1[actionIndex] = menuActionIndex1[actionIndex + 1];
							menuActionIndex1[actionIndex + 1] = i_462_;
							bool = false;
						}
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("40707, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final int method83(int i, int i_463_, int i_464_) {
		try {
			int i_465_ = 256 - i_464_;
			return ((i & 0xff00ff) * i_465_ + (i_463_ & 0xff00ff) * i_464_ & ~0xff00ff)
					+ ((i & 0xff00) * i_465_ + (i_463_ & 0xff00) * i_464_ & 0xff0000) >> 8;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("76365, " + i + ", " + i_463_ + ", " + i_464_ + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method84(String username, String password, boolean loginType) {
		SignLink.lastUsername = username;
		try {
			if (!loginType) {
				loginMessage1 = "";
				loginMessage2 = "Connecting to server...";
				renderLoginScreen(true);
			}
			bufferedConnection = new BufferedConnection(this, createSocket(43594 + Client.portOffset));
			long nameLong = TextUtils.nameToLong(username);
			int i = (int) (nameLong >> 16 & 0x1fL);
			outBuffer.offset = 0;
			outBuffer.put(14);
			outBuffer.put(i);
			bufferedConnection.write(2, outBuffer.payload, 0);
			for (int i_467_ = 0; i_467_ < 8; i_467_++) {
				bufferedConnection.read();
			}
			int loginResponse = bufferedConnection.read();
			int i_469_ = loginResponse;
			if (loginResponse == 0) {
				bufferedConnection.read(inBuffer.payload, 0, 8);
				inBuffer.offset = 0;
				aLong1240 = inBuffer.getLong();
				int[] seed = new int[4];
				seed[0] = (int) (Math.random() * 9.9999999E7);
				seed[1] = (int) (Math.random() * 9.9999999E7);
				seed[2] = (int) (aLong1240 >> 32);
				seed[3] = (int) aLong1240;
				outBuffer.offset = 0;
				outBuffer.put(10);
				outBuffer.putInt(seed[0]);
				outBuffer.putInt(seed[1]);
				outBuffer.putInt(seed[2]);
				outBuffer.putInt(seed[3]);
				outBuffer.putInt(SignLink.uid);
				outBuffer.putString(username);
				outBuffer.putString(password);
				outBuffer.applyRSA(Client.RSA_EXPONENT, Client.RSA_MODULUS);
				aBuffer872.offset = 0;
				if (loginType) {
					aBuffer872.put(18);
				} else {
					aBuffer872.put(16);
				}
				aBuffer872.put(outBuffer.offset + 36 + 1 + 1 + 2);
				aBuffer872.put(255);
				aBuffer872.putShort(317);
				aBuffer872.put(Client.lowMemory ? 1 : 0);
				for (int crcValue = 0; crcValue < 9; crcValue++) {
					aBuffer872.putInt(crcValues[crcValue]);
				}
				aBuffer872.putBytes(outBuffer.payload, outBuffer.offset, 0);
				outBuffer.isaacCipher = new ISAACCipher(seed);
				for (int index_ = 0; index_ < 4; index_++) {
					seed[index_] += 50;
				}
				isaacCipher = new ISAACCipher(seed);
				bufferedConnection.write(aBuffer872.offset, aBuffer872.payload, 0);
				loginResponse = bufferedConnection.read();
			}
			if (loginResponse == 1) {
				try {
					Thread.sleep(2000L);
				} catch (Exception exception) {
					/* empty */
				}
				method84(username, password, loginType);
			} else if (loginResponse == 2) {
				playerRights = bufferedConnection.read();
				Client.flagged = bufferedConnection.read() == 1;
				aLong1245 = 0L;
				anInt1047 = 0;
				mouseCapturer.coord = 0;
				awtFocus = true;
				aBoolean979 = true;
				loggedIn = true;
				outBuffer.offset = 0;
				inBuffer.offset = 0;
				opcode = -1;
				anInt866 = -1;
				anInt867 = -1;
				anInt868 = -1;
				packetSize = 0;
				anInt1034 = 0;
				systemUpdateTime = 0;
				anInt1036 = 0;
				hintIconType = 0;
				menuActionRow = 0;
				actionMenuOpen = false;
				idleTime = 0;
				for (int i_472_ = 0; i_472_ < 100; i_472_++) {
					chatboxMessage[i_472_] = null;
				}
				anInt1307 = 0;
				anInt1161 = 0;
				anInt1048 = 0;
				trackCount = 0;
				anInt1303 = (int) (Math.random() * 100.0) - 50;
				anInt1156 = (int) (Math.random() * 110.0) - 55;
				anInt921 = (int) (Math.random() * 80.0) - 40;
				anInt1234 = (int) (Math.random() * 120.0) - 60;
				anInt1195 = (int) (Math.random() * 30.0) - 20;
				anInt1210 = (int) (Math.random() * 20.0) - 10 & 0x7ff;
				minimapState = 0;
				lastSceneIndex = -1;
				destinationX = 0;
				destinationY = 0;
				playerCount = 0;
				actorCount = 0;
				for (int playerId = 0; playerId < MAX_PLAYERS; playerId++) {
					players[playerId] = null;
					playerBuffer[playerId] = null;
				}
				for (int npcId = 0; npcId < 16384; npcId++) {
					actors[npcId] = null;
				}
				Client.localPlayer = players[localPlayerIndex] = new Player();
				projectileList.clear();
				aLinkedList1081.clear();
				for (int i_475_ = 0; i_475_ < 4; i_475_++) {
					for (int i_476_ = 0; i_476_ < 104; i_476_++) {
						for (int i_477_ = 0; i_477_ < 104; i_477_++) {
							groundItemNodes[i_475_][i_476_][i_477_] = null;
						}
					}
				}
				aLinkedList1204 = new LinkedList();
				friendListStatus = 0;
				friendsListCount = 0;
				anInt1067 = -1;
				chatboxWidgetIndex = -1;
				openWidgetIndex = -1;
				anInt1214 = -1;
				walkablewidgetIndex = -1;
				aBoolean1174 = false;
				currentTabIndex = 3;
				inputType = 0;
				actionMenuOpen = false;
				aBoolean1281 = false;
				aString869 = null;
				anInt1080 = 0;
				flashingSidebar = -1;
				aBoolean1072 = true;
				method45(0);
				for (int i_478_ = 0; i_478_ < 5; i_478_++) {
					anIntArray1015[i_478_] = 0;
				}
				for (int i_479_ = 0; i_479_ < 5; i_479_++) {
					playerActions[i_479_] = null;
					aBooleanArray1153[i_479_] = false;
				}
				Client.anInt1200 = 0;
				Client.anInt1159 = 0;
				Client.anInt1011 = 0;
				Client.currentWalkingQueueSize = 0;
				Client.anInt949 = 0;
				Client.anInt1213 = 0;
				Client.anInt1180 = 0;
				Client.anInt1251 = 0;
				method79(1);
			} else if (loginResponse == 3) {
				loginMessage1 = "";
				loginMessage2 = "Invalid username or password.";
			} else if (loginResponse == 4) {
				loginMessage1 = "Your account has been disabled.";
				loginMessage2 = "Please check your message-centre for details.";
			} else if (loginResponse == 5) {
				loginMessage1 = "Your account is already logged in.";
				loginMessage2 = "Try again in 60 secs...";
			} else if (loginResponse == 6) {
				loginMessage1 = "RuneScape has been updated!";
				loginMessage2 = "Please reload this page.";
			} else if (loginResponse == 7) {
				loginMessage1 = "This world is full.";
				loginMessage2 = "Please use a different world.";
			} else if (loginResponse == 8) {
				loginMessage1 = "Unable to connect.";
				loginMessage2 = "Login server offline.";
			} else if (loginResponse == 9) {
				loginMessage1 = "Login limit exceeded.";
				loginMessage2 = "Too many connections from your address.";
			} else if (loginResponse == 10) {
				loginMessage1 = "Unable to connect.";
				loginMessage2 = "Bad session id.";
			} else if (loginResponse == 11) {
				loginMessage2 = "Login server rejected session.";
				loginMessage2 = "Please try again.";
			} else if (loginResponse == 12) {
				loginMessage1 = "You need a members account to login to this world.";
				loginMessage2 = "Please subscribe, or use a different world.";
			} else if (loginResponse == 13) {
				loginMessage1 = "Could not complete login.";
				loginMessage2 = "Please try using a different world.";
			} else if (loginResponse == 14) {
				loginMessage1 = "The server is being updated.";
				loginMessage2 = "Please wait 1 minute and try again.";
			} else if (loginResponse == 15) {
				loggedIn = true;
				outBuffer.offset = 0;
				inBuffer.offset = 0;
				opcode = -1;
				anInt866 = -1;
				anInt867 = -1;
				anInt868 = -1;
				packetSize = 0;
				anInt1034 = 0;
				systemUpdateTime = 0;
				menuActionRow = 0;
				actionMenuOpen = false;
				aLong849 = System.currentTimeMillis();
			} else if (loginResponse == 16) {
				loginMessage1 = "Login attempts exceeded.";
				loginMessage2 = "Please wait 1 minute and try again.";
			} else if (loginResponse == 17) {
				loginMessage1 = "You are standing in a members-only area.";
				loginMessage2 = "To play on this world move to a free area first";
			} else if (loginResponse == 20) {
				loginMessage1 = "Invalid loginserver requested";
				loginMessage2 = "Please try using a different world.";
			} else if (loginResponse == 21) {
				for (int i_480_ = bufferedConnection.read(); i_480_ >= 0; i_480_--) {
					loginMessage1 = "You have only just left another world";
					loginMessage2 = "Your profile will be transferred in: " + i_480_ + " seconds";
					renderLoginScreen(true);
					try {
						Thread.sleep(1000L);
					} catch (Exception exception) {
						/* empty */
					}
				}
				method84(username, password, loginType);
			} else if (loginResponse == -1) {
				if (i_469_ == 0) {
					if (anInt1063 < 2) {
						try {
							Thread.sleep(2000L);
						} catch (Exception exception) {
							/* empty */
						}
						anInt1063++;
						method84(username, password, loginType);
					} else {
						loginMessage1 = "No response from loginserver";
						loginMessage2 = "Please wait 1 minute and try again.";
					}
				} else {
					loginMessage1 = "No response from server";
					loginMessage2 = "Please try using a different world.";
				}
			} else {
				System.out.println("response:" + loginResponse);
				loginMessage1 = "Unexpected server response";
				loginMessage2 = "Please try using a different world.";
			}
		} catch (IOException ioexception) {
			loginMessage1 = "";
			loginMessage2 = "Error connecting to server.";
		}
	}

	public final boolean calculatePath(int clickType, int objectRotation, int objectSizeY, int objectType,
			int fromLocalY, int objectSizeX, int objectFace, int toLocalY, int fromLocalX,
			boolean isArbitraryDestination, int toLocalX) {
		int mapSizeX = 104;
		int mapSizeY = 104;
		for (int x = 0; x < mapSizeX; x++) {
			for (int y = 0; y < mapSizeY; y++) {
				wayPoints[x][y] = 0;
				distanceValues[x][y] = 99999999;
			}
		}
		int currentX = fromLocalX;
		int currentY = fromLocalY;
		wayPoints[fromLocalX][fromLocalY] = 99;
		distanceValues[fromLocalX][fromLocalY] = 0;
		int nextIndex = 0;
		int currentIndex = 0;
		walkingQueueX[nextIndex] = fromLocalX;
		walkingQueueY[nextIndex++] = fromLocalY;
		boolean foundDesination = false;
		int maxPathSize = walkingQueueX.length;
		int[][] clippingFlags = currentCollisionMap[currentSceneIndex].adjacency;
		while (currentIndex != nextIndex) {
			currentX = walkingQueueX[currentIndex];
			currentY = walkingQueueY[currentIndex];
			currentIndex = (currentIndex + 1) % maxPathSize;
			if (currentX == toLocalX && currentY == toLocalY) {
				foundDesination = true;
				break;
			}
			if (objectType != 0) {
				if ((objectType < 5 || objectType == 10)
						&& currentCollisionMap[currentSceneIndex].reachedWall(currentX, currentY, toLocalX, toLocalY,
								objectType - 1, objectRotation)) {
					foundDesination = true;
					break;
				}
				if (objectType < 10
						&& currentCollisionMap[currentSceneIndex].reachedWallDecoration(currentX, currentY, toLocalX,
								toLocalY, objectType - 1, objectRotation)) {
					foundDesination = true;
					break;
				}
			}
			if (objectSizeX != 0
					&& objectSizeY != 0
					&& currentCollisionMap[currentSceneIndex].reachedFacingObject(currentX, currentY, toLocalX,
							toLocalY, objectSizeX, objectSizeY, objectFace)) {
				foundDesination = true;
				break;
			}
			int newDistanceValue = distanceValues[currentX][currentY] + 1;
			if (currentX > 0 && wayPoints[currentX - 1][currentY] == 0
					&& (clippingFlags[currentX - 1][currentY] & 0x1280108) == 0) {
				walkingQueueX[nextIndex] = currentX - 1;
				walkingQueueY[nextIndex] = currentY;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX - 1][currentY] = 2;
				distanceValues[currentX - 1][currentY] = newDistanceValue;
			}
			if (currentX < mapSizeX - 1 && wayPoints[currentX + 1][currentY] == 0
					&& (clippingFlags[currentX + 1][currentY] & 0x1280180) == 0) {
				walkingQueueX[nextIndex] = currentX + 1;
				walkingQueueY[nextIndex] = currentY;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX + 1][currentY] = 8;
				distanceValues[currentX + 1][currentY] = newDistanceValue;
			}
			if (currentY > 0 && wayPoints[currentX][currentY - 1] == 0
					&& (clippingFlags[currentX][currentY - 1] & 0x1280102) == 0) {
				walkingQueueX[nextIndex] = currentX;
				walkingQueueY[nextIndex] = currentY - 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX][currentY - 1] = 1;
				distanceValues[currentX][currentY - 1] = newDistanceValue;
			}
			if (currentY < mapSizeY - 1 && wayPoints[currentX][currentY + 1] == 0
					&& (clippingFlags[currentX][currentY + 1] & 0x1280120) == 0) {
				walkingQueueX[nextIndex] = currentX;
				walkingQueueY[nextIndex] = currentY + 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX][currentY + 1] = 4;
				distanceValues[currentX][currentY + 1] = newDistanceValue;
			}
			if (currentX > 0 && currentY > 0 && wayPoints[currentX - 1][currentY - 1] == 0
					&& (clippingFlags[currentX - 1][currentY - 1] & 0x128010e) == 0
					&& (clippingFlags[currentX - 1][currentY] & 0x1280108) == 0
					&& (clippingFlags[currentX][currentY - 1] & 0x1280102) == 0) {
				walkingQueueX[nextIndex] = currentX - 1;
				walkingQueueY[nextIndex] = currentY - 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX - 1][currentY - 1] = 3;
				distanceValues[currentX - 1][currentY - 1] = newDistanceValue;
			}
			if (currentX < mapSizeX - 1 && currentY > 0 && wayPoints[currentX + 1][currentY - 1] == 0
					&& (clippingFlags[currentX + 1][currentY - 1] & 0x1280183) == 0
					&& (clippingFlags[currentX + 1][currentY] & 0x1280180) == 0
					&& (clippingFlags[currentX][currentY - 1] & 0x1280102) == 0) {
				walkingQueueX[nextIndex] = currentX + 1;
				walkingQueueY[nextIndex] = currentY - 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX + 1][currentY - 1] = 9;
				distanceValues[currentX + 1][currentY - 1] = newDistanceValue;
			}
			if (currentX > 0 && currentY < mapSizeY - 1 && wayPoints[currentX - 1][currentY + 1] == 0
					&& (clippingFlags[currentX - 1][currentY + 1] & 0x1280138) == 0
					&& (clippingFlags[currentX - 1][currentY] & 0x1280108) == 0
					&& (clippingFlags[currentX][currentY + 1] & 0x1280120) == 0) {
				walkingQueueX[nextIndex] = currentX - 1;
				walkingQueueY[nextIndex] = currentY + 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX - 1][currentY + 1] = 6;
				distanceValues[currentX - 1][currentY + 1] = newDistanceValue;
			}
			if (currentX < mapSizeX - 1 && currentY < mapSizeY - 1 && wayPoints[currentX + 1][currentY + 1] == 0
					&& (clippingFlags[currentX + 1][currentY + 1] & 0x12801e0) == 0
					&& (clippingFlags[currentX + 1][currentY] & 0x1280180) == 0
					&& (clippingFlags[currentX][currentY + 1] & 0x1280120) == 0) {
				walkingQueueX[nextIndex] = currentX + 1;
				walkingQueueY[nextIndex] = currentY + 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX + 1][currentY + 1] = 12;
				distanceValues[currentX + 1][currentY + 1] = newDistanceValue;
			}
		}
		arbitraryDestination = 0;
		if (!foundDesination) {
			if (isArbitraryDestination) {
				int maxStepsNonInclusive = 100;
				for (int deviation = 1; deviation < 2; deviation++) {
					for (int xDeviation = toLocalX - deviation; xDeviation <= toLocalX + deviation; xDeviation++) {
						for (int yDeviation = toLocalY - deviation; yDeviation <= toLocalY + deviation; yDeviation++) {
							if (xDeviation >= 0 && yDeviation >= 0 && xDeviation < 104 && yDeviation < 104
									&& distanceValues[xDeviation][yDeviation] < maxStepsNonInclusive) {
								maxStepsNonInclusive = distanceValues[xDeviation][yDeviation];
								currentX = xDeviation;
								currentY = yDeviation;
								arbitraryDestination = 1;
								foundDesination = true;
							}
						}
					}
					if (foundDesination) {
						break;
					}
				}
			}
			if (!foundDesination) {
				return false;
			}
		}
		currentIndex = 0;
		walkingQueueX[currentIndex] = currentX;
		walkingQueueY[currentIndex++] = currentY;
		int initialSkipCheck;
		int waypoint = initialSkipCheck = wayPoints[currentX][currentY];
		while (currentX != fromLocalX || currentY != fromLocalY) {
			if (waypoint != initialSkipCheck) {
				initialSkipCheck = waypoint;
				walkingQueueX[currentIndex] = currentX;
				walkingQueueY[currentIndex++] = currentY;
			}
			if ((waypoint & 0x2) != 0) {
				currentX++;
			} else if ((waypoint & 0x8) != 0) {
				currentX--;
			}
			if ((waypoint & 0x1) != 0) {
				currentY++;
			} else if ((waypoint & 0x4) != 0) {
				currentY--;
			}
			waypoint = wayPoints[currentX][currentY];
		}
		if (currentIndex > 0) {
			maxPathSize = currentIndex;
			if (maxPathSize > 25) {
				maxPathSize = 25;
			}
			currentIndex--;
			int localX = walkingQueueX[currentIndex];
			int localY = walkingQueueY[currentIndex];
			Client.currentWalkingQueueSize += maxPathSize;
			if (Client.currentWalkingQueueSize >= 92) {
				outBuffer.putOpcode(36);
				outBuffer.putInt(0);
				Client.currentWalkingQueueSize = 0;
			}
			if (clickType == 0) {
				outBuffer.putOpcode(164);
				outBuffer.put(maxPathSize + maxPathSize + 3);
			}
			if (clickType == 1) {
				outBuffer.putOpcode(248);
				outBuffer.put(maxPathSize + maxPathSize + 3 + 14);
			}
			if (clickType == 2) {
				outBuffer.putOpcode(98);
				outBuffer.put(maxPathSize + maxPathSize + 3);
			}
			outBuffer.putLEShortA(localX + regionAbsoluteBaseX);
			destinationX = walkingQueueX[0];
			destinationY = walkingQueueY[0];
			for (int maxPathSizeCounter = 1; maxPathSizeCounter < maxPathSize; maxPathSizeCounter++) {
				currentIndex--;
				outBuffer.put(walkingQueueX[currentIndex] - localX);
				outBuffer.put(walkingQueueY[currentIndex] - localY);
			}
			outBuffer.putLEShort(localY + regionAbsoluteBaseY);
			outBuffer.putByteC(keyStatus[5] == 1 ? 1 : 0);
			return true;
		}
		if (clickType == 1) {
			return false;
		}
		return true;
	}

	private final void method86(int i, Buffer buffer, boolean bool) {
		try {
			for (int i_512_ = 0; i_512_ < anInt918; i_512_++) {
				int i_513_ = anIntArray919[i_512_];
				Npc npc = actors[i_513_];
				int i_514_ = buffer.getUnsignedByte();
				if ((i_514_ & 0x10) != 0) {
					int i_515_ = buffer.getUnsignedShort();
					if (i_515_ == 65535) {
						i_515_ = -1;
					}
					int i_516_ = buffer.getUnsignedByte();
					if (i_515_ == npc.animation && i_515_ != -1) {
						int i_517_ = AnimationSequence.cache[i_515_].anInt63;
						if (i_517_ == 1) {
							npc.anInt1547 = 0;
							npc.anInt1548 = 0;
							npc.aniomationDelay = i_516_;
							npc.anInt1550 = 0;
						}
						if (i_517_ == 2) {
							npc.anInt1550 = 0;
						}
					} else if (i_515_ == -1
							|| npc.animation == -1
							|| AnimationSequence.cache[i_515_].anInt57 >= AnimationSequence.cache[npc.animation].anInt57) {
						npc.animation = i_515_;
						npc.anInt1547 = 0;
						npc.anInt1548 = 0;
						npc.aniomationDelay = i_516_;
						npc.anInt1550 = 0;
						npc.anInt1562 = npc.pathLength;
					}
				}
				if ((i_514_ & 0x8) != 0) {
					int i_518_ = buffer.getUnsignedByteA();
					int i_519_ = buffer.getUnsignedByteC();
					npc.updateHits(i_519_, i_518_, Client.currentCycle);
					npc.endCycle = Client.currentCycle + 300;
					npc.maxHealth = buffer.getUnsignedByteA();
					npc.currentHealth = buffer.getUnsignedByte();
				}
				if ((i_514_ & 0x80) != 0) {
					npc.spotAnimationId = buffer.getUnsignedLEShort();
					int i_520_ = buffer.getInt();
					npc.spotAnimationDelay = i_520_ >> 16;
					npc.spotAnimationEndCycle = Client.currentCycle + (i_520_ & 0xffff);
					npc.currentAnimationFrame = 0;
					npc.anInt1542 = 0;
					if (npc.spotAnimationEndCycle > Client.currentCycle) {
						npc.currentAnimationFrame = -1;
					}
					if (npc.spotAnimationId == 65535) {
						npc.spotAnimationId = -1;
					}
				}
				if ((i_514_ & 0x20) != 0) {
					npc.interactingEntity = buffer.getUnsignedLEShort();
					if (npc.interactingEntity == 65535) {
						npc.interactingEntity = -1;
					}
				}
				if ((i_514_ & 0x1) != 0) {
					npc.forcedChat = buffer.getString();
					npc.anInt1555 = 100;
				}
				if ((i_514_ & 0x40) != 0) {
					int i_521_ = buffer.getUnsignedByteC();
					int i_522_ = buffer.getUnsignedByteS();
					npc.updateHits(i_522_, i_521_, Client.currentCycle);
					npc.endCycle = Client.currentCycle + 300;
					npc.maxHealth = buffer.getUnsignedByteS();
					npc.currentHealth = buffer.getUnsignedByteC();
				}
				if ((i_514_ & 0x2) != 0) {
					npc.npcDefinition = ActorDefinition.getDefinition(buffer.getUnsignedShortA());
					npc.boundaryDimension = npc.npcDefinition.boundaryDimension;
					npc.anInt1524 = npc.npcDefinition.degreesToTurn;
					npc.walkAnimationId = npc.npcDefinition.walkAnimationId;
					npc.turnAroundAnimationId = npc.npcDefinition.turnAroundAnimationId;
					npc.turnRightAnimationId = npc.npcDefinition.turnRightAnimationId;
					npc.turnLeftAnimationId = npc.npcDefinition.turnLeftAnimationId;
					npc.standAnimationId = npc.npcDefinition.standAnimationId;
				}
				if ((i_514_ & 0x4) != 0) {
					npc.faceTowardX = buffer.getUnsignedShort();
					npc.faceTowardY = buffer.getUnsignedShort();
				}
			}
			loggedIn &= bool;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("51191, " + i + ", " + buffer + ", " + bool + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method87(ActorDefinition npcdefinition, int i, boolean bool, int i_523_, int i_524_) {
		try {
			if (menuActionRow < 400) {
				if (npcdefinition.childrenIndexes != null) {
					npcdefinition = npcdefinition.getChildDefinition();
				}
				if (npcdefinition != null && npcdefinition.clickable) {
					String string = npcdefinition.name;
					if (npcdefinition.combatLevel != 0) {
						string += Client.method110(Client.localPlayer.combatLevel, npcdefinition.combatLevel, true)
								+ " (level-" + npcdefinition.combatLevel + ")";
					}
					if (anInt1307 == 1) {
						menuActionNames[menuActionRow] = "Use " + aString1311 + " with @yel@" + string;
						menuActionIndexes[menuActionRow] = 582;
						menuActionIndex1[menuActionRow] = i;
						menuActionIndex2[menuActionRow] = i_524_;
						menuActionIndex3[menuActionRow] = i_523_;
						menuActionRow++;
					} else if (anInt1161 == 1) {
						if ((anInt1163 & 0x2) == 2) {
							menuActionNames[menuActionRow] = aString1164 + " @yel@" + string;
							menuActionIndexes[menuActionRow] = 413;
							menuActionIndex1[menuActionRow] = i;
							menuActionIndex2[menuActionRow] = i_524_;
							menuActionIndex3[menuActionRow] = i_523_;
							menuActionRow++;
						}
					} else {
						if (npcdefinition.actions != null) {
							for (int i_525_ = 4; i_525_ >= 0; i_525_--) {
								if (npcdefinition.actions[i_525_] != null
										&& !npcdefinition.actions[i_525_].equalsIgnoreCase("attack")) {
									menuActionNames[menuActionRow] = npcdefinition.actions[i_525_] + " @yel@" + string;
									if (i_525_ == 0) {
										menuActionIndexes[menuActionRow] = 20;
									}
									if (i_525_ == 1) {
										menuActionIndexes[menuActionRow] = 412;
									}
									if (i_525_ == 2) {
										menuActionIndexes[menuActionRow] = 225;
									}
									if (i_525_ == 3) {
										menuActionIndexes[menuActionRow] = 965;
									}
									if (i_525_ == 4) {
										menuActionIndexes[menuActionRow] = 478;
									}
									menuActionIndex1[menuActionRow] = i;
									menuActionIndex2[menuActionRow] = i_524_;
									menuActionIndex3[menuActionRow] = i_523_;
									menuActionRow++;
								}
							}
						}
						if (npcdefinition.actions != null) {
							for (int i_526_ = 4; i_526_ >= 0; i_526_--) {
								if (npcdefinition.actions[i_526_] != null
										&& npcdefinition.actions[i_526_].equalsIgnoreCase("attack")) {
									int i_527_ = 0;
									if (npcdefinition.combatLevel > Client.localPlayer.combatLevel) {
										i_527_ = 2000;
									}
									menuActionNames[menuActionRow] = npcdefinition.actions[i_526_] + " @yel@" + string;
									if (i_526_ == 0) {
										menuActionIndexes[menuActionRow] = 20 + i_527_;
									}
									if (i_526_ == 1) {
										menuActionIndexes[menuActionRow] = 412 + i_527_;
									}
									if (i_526_ == 2) {
										menuActionIndexes[menuActionRow] = 225 + i_527_;
									}
									if (i_526_ == 3) {
										menuActionIndexes[menuActionRow] = 965 + i_527_;
									}
									if (i_526_ == 4) {
										menuActionIndexes[menuActionRow] = 478 + i_527_;
									}
									menuActionIndex1[menuActionRow] = i;
									menuActionIndex2[menuActionRow] = i_524_;
									menuActionIndex3[menuActionRow] = i_523_;
									menuActionRow++;
								}
							}
						}
						menuActionNames[menuActionRow] = "Examine @yel@" + string;
						menuActionIndexes[menuActionRow] = 1025;
						menuActionIndex1[menuActionRow] = i;
						menuActionIndex2[menuActionRow] = i_524_;
						menuActionIndex3[menuActionRow] = i_523_;
						menuActionRow++;
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("36696, " + npcdefinition + ", " + i + ", " + bool + ", " + i_523_ + ", " + i_524_
					+ ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method88(int i, int i_528_, Player player, boolean bool, int i_529_) {
		try {
			if (player != Client.localPlayer && menuActionRow < 400 && !bool) {
				String string;
				if (player.anInt1743 == 0) {
					string = player.playerName
							+ Client.method110(Client.localPlayer.combatLevel, player.combatLevel, true) + " (level-"
							+ player.combatLevel + ")";
				} else {
					string = player.playerName + " (skill-" + player.anInt1743 + ")";
				}
				if (anInt1307 == 1) {
					menuActionNames[menuActionRow] = "Use " + aString1311 + " with @whi@" + string;
					menuActionIndexes[menuActionRow] = 491;
					menuActionIndex1[menuActionRow] = i_528_;
					menuActionIndex2[menuActionRow] = i;
					menuActionIndex3[menuActionRow] = i_529_;
					menuActionRow++;
				} else if (anInt1161 == 1) {
					if ((anInt1163 & 0x8) == 8) {
						menuActionNames[menuActionRow] = aString1164 + " @whi@" + string;
						menuActionIndexes[menuActionRow] = 365;
						menuActionIndex1[menuActionRow] = i_528_;
						menuActionIndex2[menuActionRow] = i;
						menuActionIndex3[menuActionRow] = i_529_;
						menuActionRow++;
					}
				} else {
					for (int action = 4; action >= 0; action--) {
						if (playerActions[action] != null) {
							menuActionNames[menuActionRow] = playerActions[action] + " @whi@" + string;
							int i_531_ = 0;
							if (playerActions[action].equalsIgnoreCase("attack")) {
								if (player.combatLevel > Client.localPlayer.combatLevel) {
									i_531_ = 2000;
								}
								if (Client.localPlayer.teamIndex != 0 && player.teamIndex != 0) {
									if (Client.localPlayer.teamIndex == player.teamIndex) {
										i_531_ = 2000;
									} else {
										i_531_ = 0;
									}
								}
							} else if (aBooleanArray1153[action]) {
								i_531_ = 2000;
							}
							if (action == 0) {
								menuActionIndexes[menuActionRow] = 561 + i_531_;
							}
							if (action == 1) {
								menuActionIndexes[menuActionRow] = 779 + i_531_;
							}
							if (action == 2) {
								menuActionIndexes[menuActionRow] = 27 + i_531_;
							}
							if (action == 3) {
								menuActionIndexes[menuActionRow] = 577 + i_531_;
							}
							if (action == 4) {
								menuActionIndexes[menuActionRow] = 729 + i_531_;
							}
							menuActionIndex1[menuActionRow] = i_528_;
							menuActionIndex2[menuActionRow] = i;
							menuActionIndex3[menuActionRow] = i_529_;
							menuActionRow++;
						}
					}
				}
				for (int i_532_ = 0; i_532_ < menuActionRow; i_532_++) {
					if (menuActionIndexes[i_532_] == 516) {
						menuActionNames[i_532_] = "Walk here @whi@" + string;
						break;
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("46972, " + i + ", " + i_528_ + ", " + player + ", " + bool + ", " + i_529_ + ", "
					+ runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	private final void method89(SpawnObjectNode spawnOjectNode) {
		int uid = 0;
		int id = -1;
		int type = 0;
		int face = 0;
		if (spawnOjectNode.type == 0) {
			uid = currentScene.method522(spawnOjectNode.plane, spawnOjectNode.x, spawnOjectNode.y);
		}
		if (spawnOjectNode.type == 1) {
			uid = currentScene.method523(spawnOjectNode.plane, spawnOjectNode.x, 0, spawnOjectNode.y);
		}
		if (spawnOjectNode.type == 2) {
			uid = currentScene.method524(spawnOjectNode.plane, spawnOjectNode.x, spawnOjectNode.y);
		}
		if (spawnOjectNode.type == 3) {
			uid = currentScene.getFloorHash(spawnOjectNode.plane, spawnOjectNode.x, spawnOjectNode.y);
		}
		if (uid != 0) {
			int tag = currentScene.method526(spawnOjectNode.plane, spawnOjectNode.x, spawnOjectNode.y, uid);
			id = uid >> 14 & 0x7fff;
			type = tag & 0x1f;
			face = tag >> 6;
		}
		spawnOjectNode.id = id;
		spawnOjectNode.anInt1351 = type;
		spawnOjectNode.face = face;
	}

	public final void method90() {
		try {
			for (int track = 0; track < trackCount; track++) {
				if (trackDelay[track] <= 0) {
					boolean flag = false;
					try {
						if (trackIndexes[track] == anInt899 && trackLoop[track] == anInt1314) {
							if (!SignLink.rewriteWave()) {
								flag = true;
							}
						} else {
							Buffer buffer = SoundTrack.data(trackIndexes[track], trackLoop[track]);
							if (System.currentTimeMillis() + buffer.offset / 22 > aLong1197 + anInt1282 / 22) {
								anInt1282 = buffer.offset;
								aLong1197 = System.currentTimeMillis();
								if (SignLink.writeWave(buffer.payload, buffer.offset)) {
									anInt899 = trackIndexes[track];
									anInt1314 = trackLoop[track];
								} else {
									flag = true;
								}
							}
						}
					} catch (Exception exception) {
						/* empty */
					}
					if (!flag || trackDelay[track] == -5) {
						trackCount--;
						for (int i_539_ = track; i_539_ < trackCount; i_539_++) {
							trackIndexes[i_539_] = trackIndexes[i_539_ + 1];
							trackLoop[i_539_] = trackLoop[i_539_ + 1];
							trackDelay[i_539_] = trackDelay[i_539_ + 1];
						}
						track--;
					} else {
						trackDelay[track] = -5;
					}
				} else {
					trackDelay[track]--;
				}
			}
			if (songFadeCycle <= 0) {
				return;
			}
			songFadeCycle -= 20;
			if (songFadeCycle < 0) {
				songFadeCycle = 0;
			}
			if (songFadeCycle != 0 || !musicEnabled || Client.lowMemory) {
				return;
			}
			onDemandRequesterId = songIndex;
			midiFade = true;
			onDemandRequester.request(2, onDemandRequesterId);
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("65150, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	@Override
	public final void startup() {
		drawLoadingText(20, "Starting up");
		if (SignLink.mainCache != null) {
			for (int i = 0; i < 5; i++) {
				stores[i] = new Index(SignLink.mainCache, SignLink.cacheIndexes[i], i + 1);
			}
		}
		try {
			if (Client.JAGGRAB) {
				verifyArchives();
			}
			anArchive1078 = requestArchive(1, "title screen", "title", crcValues[1], 25);
			fontSmall = new TypeFace(false, "p11_full", anArchive1078);
			fontNormal = new TypeFace(false, "p12_full", anArchive1078);
			fontBold = new TypeFace(false, "b12_full", anArchive1078);
			fontFancy = new TypeFace(true, "q8_full", anArchive1078);
			prepareTitleBackground();
			prepareTitle();
			Archive archiveConfig = requestArchive(2, "config", "config", crcValues[2], 30);
			Archive archiveInterface = requestArchive(3, "interface", "interface", crcValues[3], 35);
			Archive archiveMedia = requestArchive(4, "2d graphics", "media", crcValues[4], 40);
			Archive archiveTextures = requestArchive(6, "textures", "textures", crcValues[6], 45);
			Archive archiveCensor = requestArchive(7, "chat system", "wordenc", crcValues[7], 50);
			Archive archiveAudio = requestArchive(8, "sound effects", "sounds", crcValues[8], 55);
			currentSceneTileFlags = new byte[4][104][104];
			anIntArrayArrayArray1239 = new int[4][105][105];
			currentScene = new Scene(104, (byte) 43, 104, anIntArrayArrayArray1239, 4);
			for (int i = 0; i < 4; i++) {
				currentCollisionMap[i] = new CollisionMap(104, 104, true);
			}
			minimapImage = new ImageRGB(512, 512);
			Archive archiveCRC = requestArchive(5, "update list", "versionlist", crcValues[5], 60);
			drawLoadingText(60, "Connecting to update server");
			onDemandRequester = new OnDemandRequester();
			onDemandRequester.init(archiveCRC, this);
			Animation.method148(onDemandRequester.animCount());
			Model.init(onDemandRequester.fileCount(0), onDemandRequester);
			if (!Client.lowMemory) {
				onDemandRequesterId = 0;
				try {
					onDemandRequesterId = Integer.parseInt(getParameter("music"));
				} catch (Exception exception) {
					/* empty */
				}
				midiFade = true;
				onDemandRequester.request(2, onDemandRequesterId);
				while (onDemandRequester.immediateRequestCount() > 0) {
					processOnDemand();
					try {
						Thread.sleep(100L);
					} catch (Exception exception) {
						/* empty */
					}
					if (onDemandRequester.requestFails > 3) {
						method28("ondemand");
						return;
					}
				}
			}
			drawLoadingText(65, "Requesting animations");
			int fileRequestCount = onDemandRequester.fileCount(1);
			for (int id = 0; id < fileRequestCount; id++) {
				onDemandRequester.request(1, id);
			}
			while (onDemandRequester.immediateRequestCount() > 0) {
				int remaining = fileRequestCount - onDemandRequester.immediateRequestCount();
				if (remaining > 0) {
					drawLoadingText(65, "Loading animations - " + remaining * 100 / fileRequestCount + "%");
				}
				processOnDemand();
				try {
					Thread.sleep(100L);
				} catch (Exception exception) {
					/* empty */
				}
				if (onDemandRequester.requestFails > 3) {
					method28("ondemand");
					return;
				}
			}
			drawLoadingText(70, "Requesting models");
			fileRequestCount = onDemandRequester.fileCount(0);
			for (int id = 0; id < fileRequestCount; id++) {
				int modelIndex = onDemandRequester.modelIndex(id);
				if ((modelIndex & 0x1) != 0) {
					onDemandRequester.request(0, id);
				}
			}
			fileRequestCount = onDemandRequester.immediateRequestCount();
			while (onDemandRequester.immediateRequestCount() > 0) {
				int remaining = fileRequestCount - onDemandRequester.immediateRequestCount();
				if (remaining > 0) {
					drawLoadingText(70, "Loading models - " + remaining * 100 / fileRequestCount + "%");
				}
				processOnDemand();
				try {
					Thread.sleep(100L);
				} catch (Exception exception) {
					/* empty */
				}
			}
			if (stores[0] != null) {
				drawLoadingText(75, "Requesting maps");
				onDemandRequester.request(3, onDemandRequester.regIndex(0, 48, 47));
				onDemandRequester.request(3, onDemandRequester.regIndex(1, 48, 47));
				onDemandRequester.request(3, onDemandRequester.regIndex(0, 48, 48));
				onDemandRequester.request(3, onDemandRequester.regIndex(1, 48, 48));
				onDemandRequester.request(3, onDemandRequester.regIndex(0, 48, 49));
				onDemandRequester.request(3, onDemandRequester.regIndex(1, 48, 49));
				onDemandRequester.request(3, onDemandRequester.regIndex(0, 47, 47));
				onDemandRequester.request(3, onDemandRequester.regIndex(1, 47, 47));
				onDemandRequester.request(3, onDemandRequester.regIndex(0, 47, 48));
				onDemandRequester.request(3, onDemandRequester.regIndex(1, 47, 48));
				onDemandRequester.request(3, onDemandRequester.regIndex(0, 148, 48));
				onDemandRequester.request(3, onDemandRequester.regIndex(1, 148, 48));
				fileRequestCount = onDemandRequester.immediateRequestCount();
				while (onDemandRequester.immediateRequestCount() > 0) {
					int remaining = fileRequestCount - onDemandRequester.immediateRequestCount();
					if (remaining > 0) {
						drawLoadingText(75, "Loading maps - " + remaining * 100 / fileRequestCount + "%");
					}
					processOnDemand();
					try {
						Thread.sleep(100L);
					} catch (Exception exception) {
						/* empty */
					}
				}
			}
			fileRequestCount = onDemandRequester.fileCount(0);
			for (int id = 0; id < fileRequestCount; id++) {
				int modelIndex = onDemandRequester.modelIndex(id);
				byte priority = 0;
				if ((modelIndex & 0x8) != 0) {
					priority = (byte) 10;
				} else if ((modelIndex & 0x20) != 0) {
					priority = (byte) 9;
				} else if ((modelIndex & 0x10) != 0) {
					priority = (byte) 8;
				} else if ((modelIndex & 0x40) != 0) {
					priority = (byte) 7;
				} else if ((modelIndex & 0x80) != 0) {
					priority = (byte) 6;
				} else if ((modelIndex & 0x2) != 0) {
					priority = (byte) 5;
				} else if ((modelIndex & 0x4) != 0) {
					priority = (byte) 4;
				}
				if ((modelIndex & 0x1) != 0) {
					priority = (byte) 3;
				}
				if (priority != 0) {
					onDemandRequester.setPriority(priority, 0, id);
				}
			}
			onDemandRequester.preloadRegions(Client.membersWorld);
			if (!Client.lowMemory) {
				fileRequestCount = onDemandRequester.fileCount(2);
				for (int id = 1; id < fileRequestCount; id++) {
					if (onDemandRequester.midiIndexEqualsOne(id)) {
						onDemandRequester.setPriority((byte) 1, 2, id);
					}
				}
			}
			drawLoadingText(80, "Unpacking media");
			inventoryBackgroundImage = new IndexedImage(archiveMedia, "invback", 0);
			chatboxBackgroundImage = new IndexedImage(archiveMedia, "chatback", 0);
			minimapBackgroundImage = new IndexedImage(archiveMedia, "mapback", 0);
			anIndexedImage1052 = new IndexedImage(archiveMedia, "backbase1", 0);
			anIndexedImage1053 = new IndexedImage(archiveMedia, "backbase2", 0);
			anIndexedImage1054 = new IndexedImage(archiveMedia, "backhmid1", 0);
			for (int i_555_ = 0; i_555_ < 13; i_555_++) {
				anIndexedImageArray972[i_555_] = new IndexedImage(archiveMedia, "sideicons", i_555_);
			}
			minimapCompass = new ImageRGB(archiveMedia, "compass", 0);
			anImageRGB1026 = new ImageRGB(archiveMedia, "mapedge", 0);
			anImageRGB1026.trim();
			try {
				for (int i_556_ = 0; i_556_ < 100; i_556_++) {
					anIndexedImageArray1085[i_556_] = new IndexedImage(archiveMedia, "mapscene", i_556_);
				}
			} catch (Exception exception) {
				/* empty */
			}
			try {
				for (int i_557_ = 0; i_557_ < 100; i_557_++) {
					worldMapHintIcons[i_557_] = new ImageRGB(archiveMedia, "mapfunction", i_557_);
				}
			} catch (Exception exception) {
				/* empty */
			}
			try {
				for (int i_558_ = 0; i_558_ < 20; i_558_++) {
					anImageRGBArray1012[i_558_] = new ImageRGB(archiveMedia, "hitmarks", i_558_);
				}
			} catch (Exception exception) {
				/* empty */
			}
			try {
				for (int i_559_ = 0; i_559_ < 20; i_559_++) {
					anImageRGBArray1120[i_559_] = new ImageRGB(archiveMedia, "headicons", i_559_);
				}
			} catch (Exception exception) {
				/* empty */
			}
			mapFlagMarker = new ImageRGB(archiveMedia, "mapmarker", 0);
			anImageRGB896 = new ImageRGB(archiveMedia, "mapmarker", 1);
			for (int i_560_ = 0; i_560_ < 8; i_560_++) {
				anImageRGBArray1175[i_560_] = new ImageRGB(archiveMedia, "cross", i_560_);
			}
			mapdotItem = new ImageRGB(archiveMedia, "mapdots", 0);
			mapdotActor = new ImageRGB(archiveMedia, "mapdots", 1);
			mapdotPlayer = new ImageRGB(archiveMedia, "mapdots", 2);
			mapdotFriend = new ImageRGB(archiveMedia, "mapdots", 3);
			mapdotTeammate = new ImageRGB(archiveMedia, "mapdots", 4);
			scrollbarUp = new IndexedImage(archiveMedia, "scrollbar", 0);
			scrollbarDown = new IndexedImage(archiveMedia, "scrollbar", 1);
			anIndexedImage1168 = new IndexedImage(archiveMedia, "redstone1", 0);
			anIndexedImage1169 = new IndexedImage(archiveMedia, "redstone2", 0);
			anIndexedImage1170 = new IndexedImage(archiveMedia, "redstone3", 0);
			anIndexedImage1171 = new IndexedImage(archiveMedia, "redstone1", 0);
			anIndexedImage1171.flipHorizontal();
			anIndexedImage1172 = new IndexedImage(archiveMedia, "redstone2", 0);
			anIndexedImage1172.flipHorizontal();
			anIndexedImage890 = new IndexedImage(archiveMedia, "redstone1", 0);
			anIndexedImage890.flipVertical();
			anIndexedImage891 = new IndexedImage(archiveMedia, "redstone2", 0);
			anIndexedImage891.flipVertical();
			anIndexedImage892 = new IndexedImage(archiveMedia, "redstone3", 0);
			anIndexedImage892.flipVertical();
			anIndexedImage893 = new IndexedImage(archiveMedia, "redstone1", 0);
			anIndexedImage893.flipHorizontal();
			anIndexedImage893.flipVertical();
			anIndexedImage894 = new IndexedImage(archiveMedia, "redstone2", 0);
			anIndexedImage894.flipHorizontal();
			anIndexedImage894.flipVertical();
			for (int i_561_ = 0; i_561_ < 2; i_561_++) {
				anIndexedImageArray1244[i_561_] = new IndexedImage(archiveMedia, "mod_icons", i_561_);
			}
			ImageRGB image = new ImageRGB(archiveMedia, "backleft1", 0);
			aProducingGraphicsBuffer928 = new ProducingGraphicsBuffer(image.width, image.height, getComponent());
			image.drawInverse(0, 0);
			image = new ImageRGB(archiveMedia, "backleft2", 0);
			aProducingGraphicsBuffer929 = new ProducingGraphicsBuffer(image.width, image.height, getComponent());
			image.drawInverse(0, 0);
			image = new ImageRGB(archiveMedia, "backright1", 0);
			aProducingGraphicsBuffer930 = new ProducingGraphicsBuffer(image.width, image.height, getComponent());
			image.drawInverse(0, 0);
			image = new ImageRGB(archiveMedia, "backright2", 0);
			aProducingGraphicsBuffer931 = new ProducingGraphicsBuffer(image.width, image.height, getComponent());
			image.drawInverse(0, 0);
			image = new ImageRGB(archiveMedia, "backtop1", 0);
			aProducingGraphicsBuffer932 = new ProducingGraphicsBuffer(image.width, image.height, getComponent());
			image.drawInverse(0, 0);
			image = new ImageRGB(archiveMedia, "backvmid1", 0);
			aProducingGraphicsBuffer933 = new ProducingGraphicsBuffer(image.width, image.height, getComponent());
			image.drawInverse(0, 0);
			image = new ImageRGB(archiveMedia, "backvmid2", 0);
			aProducingGraphicsBuffer934 = new ProducingGraphicsBuffer(image.width, image.height, getComponent());
			image.drawInverse(0, 0);
			image = new ImageRGB(archiveMedia, "backvmid3", 0);
			aProducingGraphicsBuffer935 = new ProducingGraphicsBuffer(image.width, image.height, getComponent());
			image.drawInverse(0, 0);
			image = new ImageRGB(archiveMedia, "backhmid2", 0);
			aProducingGraphicsBuffer936 = new ProducingGraphicsBuffer(image.width, image.height, getComponent());
			image.drawInverse(0, 0);
			int i_562_ = (int) (Math.random() * 21.0) - 10;
			int i_563_ = (int) (Math.random() * 21.0) - 10;
			int i_564_ = (int) (Math.random() * 21.0) - 10;
			int i_565_ = (int) (Math.random() * 41.0) - 20;
			for (int i_566_ = 0; i_566_ < 100; i_566_++) {
				if (worldMapHintIcons[i_566_] != null) {
					worldMapHintIcons[i_566_].adjustRGB(i_562_ + i_565_, i_563_ + i_565_, i_564_ + i_565_);
				}
				if (anIndexedImageArray1085[i_566_] != null) {
					anIndexedImageArray1085[i_566_].mixPalette(i_562_ + i_565_, i_563_ + i_565_, i_564_ + i_565_);
				}
			}
			drawLoadingText(83, "Unpacking textures");
			Rasterizer3D.loadIndexedImages(archiveTextures);
			Rasterizer3D.method369(0.8);
			Rasterizer3D.method364(20, true);
			drawLoadingText(86, "Unpacking config");
			AnimationSequence.load(archiveConfig);
			GameObjectDefinition.load(archiveConfig);
			FloorDefinition.load(archiveConfig);
			ItemDefinition.load(archiveConfig);
			ActorDefinition.load(archiveConfig);
			IdentityKit.load(archiveConfig);
			SpotAnimation.load(archiveConfig);
			Varp.method592(0, archiveConfig);
			VarBit.load(archiveConfig);
			ItemDefinition.playerIsMember = Client.membersWorld;
			if (!Client.lowMemory) {
				drawLoadingText(90, "Unpacking sounds");
				byte[] bs = archiveAudio.getFile("sounds.dat");
				Buffer buffer = new Buffer(bs);
				SoundTrack.load(buffer);
			}
			drawLoadingText(95, "Unpacking interfaces");
			TypeFace[] typefaces = { fontSmall, fontNormal, fontBold, fontFancy };
			Widget.load(archiveInterface, typefaces, archiveMedia);
			drawLoadingText(100, "Preparing game engine");
			for (int i_567_ = 0; i_567_ < 33; i_567_++) {
				int i_568_ = 999;
				int i_569_ = 0;
				for (int i_570_ = 0; i_570_ < 34; i_570_++) {
					if (minimapBackgroundImage.pixels[i_570_ + i_567_ * minimapBackgroundImage.width] == 0) {
						if (i_568_ == 999) {
							i_568_ = i_570_;
						}
					} else if (i_568_ != 999) {
						i_569_ = i_570_;
						break;
					}
				}
				anIntArray993[i_567_] = i_568_;
				anIntArray1082[i_567_] = i_569_ - i_568_;
			}
			for (int i_571_ = 5; i_571_ < 156; i_571_++) {
				int i_572_ = 999;
				int i_573_ = 0;
				for (int i_574_ = 25; i_574_ < 172; i_574_++) {
					if (minimapBackgroundImage.pixels[i_574_ + i_571_ * minimapBackgroundImage.width] == 0
							&& (i_574_ > 34 || i_571_ > 34)) {
						if (i_572_ == 999) {
							i_572_ = i_574_;
						}
					} else if (i_572_ != 999) {
						i_573_ = i_574_;
						break;
					}
				}
				anIntArray1077[i_571_ - 5] = i_572_ - 25;
				anIntArray1254[i_571_ - 5] = i_573_ - i_572_;
			}
			Rasterizer3D.method362(-950, 479, 96);
			anIntArray1205 = Rasterizer3D.lineOffsets;
			Rasterizer3D.method362(-950, 190, 261);
			anIntArray1206 = Rasterizer3D.lineOffsets;
			Rasterizer3D.method362(-950, 512, 334);
			anIntArray1207 = Rasterizer3D.lineOffsets;
			int[] is = new int[9];
			for (int i_575_ = 0; i_575_ < 9; i_575_++) {
				int i_576_ = 128 + i_575_ * 32 + 15;
				int i_577_ = 600 + i_576_ * 3;
				int i_578_ = Rasterizer3D.SINE[i_576_];
				is[i_575_] = i_577_ * i_578_ >> 16;
			}
			Scene.method532(500, 800, 512, 334, is, Client.aBoolean1256);
			ChatCensor.load(archiveCensor);
			mouseCapturer = new MouseCapturer(this);
			startRunnable(mouseCapturer, 10);
			GameObject.aClient1600 = this;
			GameObjectDefinition.client = this;
			ActorDefinition.client = this;
		} catch (Exception exception) {
			SignLink.reportError("loaderror " + aString1074 + " " + anInt1104);
			loadingError = true;
		}
	}

	private final void method91(Buffer buffer, int i) {
		try {
			anInt1144 = -50;
			while (buffer.bitOffset + 10 < i * 8) {
				int index = buffer.getBits(11);
				if (index == 2047) {
					break;
				}
				if (players[index] == null) {
					players[index] = new Player();
					if (playerBuffer[index] != null) {
						players[index].updatePlayer(playerBuffer[index]);
					}
				}
				anIntArray917[playerCount++] = index;
				Player player = players[index];
				player.anInt1557 = Client.currentCycle;
				int i_580_ = buffer.getBits(1);
				if (i_580_ == 1) {
					anIntArray919[anInt918++] = index;
				}
				buffer.getBits(1);
				int i_582_ = buffer.getBits(5);
				if (i_582_ > 15) {
					i_582_ -= 32;
				}
				int i_583_ = buffer.getBits(5);
				if (i_583_ > 15) {
					i_583_ -= 32;
				}
				player.setPosition(Client.localPlayer.pathX[0] + i_583_, Client.localPlayer.pathY[0] + i_582_);
			}
			buffer.finishBitAccess();
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("22315, " + buffer + ", " + i + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void processClickingMinimap() {
		do {
			try {
				if (minimapState == 0) {
					if (clickType != 1) {
						break;
					}
					int i = clickX - 25 - 550;
					int i_584_ = clickY - 5 - 4;
					if (i >= 0 && i_584_ >= 0 && i < 146 && i_584_ < 151) {
						i -= 73;
						i_584_ -= 75;
						int i_585_ = anInt1210 + anInt1234 & 0x7ff;
						int i_586_ = Rasterizer3D.SINE[i_585_];
						int i_587_ = Rasterizer3D.COSINE[i_585_];
						i_586_ = i_586_ * (anInt1195 + 256) >> 8;
						i_587_ = i_587_ * (anInt1195 + 256) >> 8;
						int i_588_ = i_584_ * i_586_ + i * i_587_ >> 11;
						int i_589_ = i_584_ * i_587_ - i * i_586_ >> 11;
						int i_590_ = Client.localPlayer.xWithBoundary + i_588_ >> 7;
						int i_591_ = Client.localPlayer.yWithBoundary - i_589_ >> 7;
						boolean bool_592_ = calculatePath(1, 0, 0, 0, Client.localPlayer.pathY[0], 0, 0, i_591_,
								Client.localPlayer.pathX[0], true, i_590_);
						if (bool_592_) {
							outBuffer.put(i);
							outBuffer.put(i_584_);
							outBuffer.putShort(anInt1210);
							outBuffer.put(57);
							outBuffer.put(anInt1234);
							outBuffer.put(anInt1195);
							outBuffer.put(89);
							outBuffer.putShort(Client.localPlayer.xWithBoundary);
							outBuffer.putShort(Client.localPlayer.yWithBoundary);
							outBuffer.put(arbitraryDestination);
							outBuffer.put(63);
						}
					}
					Client.anInt1142++;
					if (Client.anInt1142 <= 1151) {
						break;
					}
					Client.anInt1142 = 0;
					outBuffer.putOpcode(246);
					outBuffer.put(0);
					int i_593_ = outBuffer.offset;
					if ((int) (Math.random() * 2.0) == 0) {
						outBuffer.put(101);
					}
					outBuffer.put(197);
					outBuffer.putShort((int) (Math.random() * 65536.0));
					outBuffer.put((int) (Math.random() * 256.0));
					outBuffer.put(67);
					outBuffer.putShort(14214);
					if ((int) (Math.random() * 2.0) == 0) {
						outBuffer.putShort(29487);
					}
					outBuffer.putShort((int) (Math.random() * 65536.0));
					if ((int) (Math.random() * 2.0) == 0) {
						outBuffer.put(220);
					}
					outBuffer.put(180);
					outBuffer.putSizeByte(outBuffer.offset - i_593_);
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("13593, " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final String method93(int i, int i_594_) {
		try {
			if (i <= 0) {
				opcode = inBuffer.getUnsignedByte();
			}
			if (i_594_ < 999999999) {
				return String.valueOf(i_594_);
			}
			return "*";
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("1025, " + i + ", " + i_594_ + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method94(int i) {
		do {
			try {
				if (i != -13873) {
					for (int i_595_ = 1; i_595_ > 0; i_595_++) {
						/* empty */
					}
				}
				Graphics graphics = getComponent().getGraphics();
				graphics.setColor(Color.black);
				graphics.fillRect(0, 0, 765, 503);
				this.method4(1);
				if (loadingError) {
					aBoolean856 = false;
					graphics.setFont(new Font("Helvetica", 1, 16));
					graphics.setColor(Color.yellow);
					int i_596_ = 35;
					graphics.drawString("Sorry, an error has occured whilst loading RuneScape", 30, i_596_);
					i_596_ += 50;
					graphics.setColor(Color.white);
					graphics.drawString("To fix this try the following (in order):", 30, i_596_);
					i_596_ += 50;
					graphics.setColor(Color.white);
					graphics.setFont(new Font("Helvetica", 1, 12));
					graphics.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, i_596_);
					i_596_ += 30;
					graphics.drawString("2: Try clearing your web-browsers cache from tools->internet options", 30,
							i_596_);
					i_596_ += 30;
					graphics.drawString("3: Try using a different game-world", 30, i_596_);
					i_596_ += 30;
					graphics.drawString("4: Try rebooting your computer", 30, i_596_);
					i_596_ += 30;
					graphics.drawString("5: Try selecting a different version of Java from the play-game menu", 30,
							i_596_);
				}
				if (genericLoadingError) {
					aBoolean856 = false;
					graphics.setFont(new Font("Helvetica", 1, 20));
					graphics.setColor(Color.white);
					graphics.drawString("Error - unable to load game!", 50, 50);
					graphics.drawString("To play RuneScape make sure you play from", 50, 100);
					graphics.drawString("http://www.runescape.com", 50, 150);
				}
				if (!alreadyLoaded) {
					break;
				}
				aBoolean856 = false;
				graphics.setColor(Color.yellow);
				int i_597_ = 35;
				graphics.drawString("Error a copy of RuneScape already appears to be loaded", 30, i_597_);
				i_597_ += 50;
				graphics.setColor(Color.white);
				graphics.drawString("To fix this try the following (in order):", 30, i_597_);
				i_597_ += 50;
				graphics.setColor(Color.white);
				graphics.setFont(new Font("Helvetica", 1, 12));
				graphics.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, i_597_);
				i_597_ += 30;
				graphics.drawString("2: Try rebooting your computer, and reloading", 30, i_597_);
				i_597_ += 30;
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("4031, " + i + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	@Override
	public final URL getCodeBase() {
		if (SignLink.applet != null) {
			return SignLink.applet.getCodeBase();
		}
		try {
			if (gameFrame != null) {
				return new URL("http://127.0.0.1:" + (80 + Client.portOffset));
			}
		} catch (Exception exception) {
			/* empty */
		}
		return super.getCodeBase();
	}

	public final void method95() {
		do {
			try {
				for (int i = 0; i < actorCount; i++) {
					int actorIndex = anIntArray862[i];
					Npc npc = actors[actorIndex];
					if (npc != null) {
						method96(npc);
					}
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("41621, " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void method96(Actor actor) {
		try {
			if (actor.xWithBoundary < 128 || actor.yWithBoundary < 128 || actor.xWithBoundary >= 13184
					|| actor.yWithBoundary >= 13184) {
				actor.animation = -1;
				actor.spotAnimationId = -1;
				actor.anInt1567 = 0;
				actor.anInt1568 = 0;
				actor.xWithBoundary = actor.pathX[0] * 128 + actor.boundaryDimension * 64;
				actor.yWithBoundary = actor.pathY[0] * 128 + actor.boundaryDimension * 64;
				actor.resetPath();
			}
			if (actor == Client.localPlayer
					&& (actor.xWithBoundary < 1536 || actor.yWithBoundary < 1536 || actor.xWithBoundary >= 11776 || actor.yWithBoundary >= 11776)) {
				actor.animation = -1;
				actor.spotAnimationId = -1;
				actor.anInt1567 = 0;
				actor.anInt1568 = 0;
				actor.xWithBoundary = actor.pathX[0] * 128 + actor.boundaryDimension * 64;
				actor.yWithBoundary = actor.pathY[0] * 128 + actor.boundaryDimension * 64;
				actor.resetPath();
			}
			if (actor.anInt1567 > Client.currentCycle) {
				method97(actor, true);
			} else if (actor.anInt1568 >= Client.currentCycle) {
				method98(actor, aByte1037);
			} else {
				method99((byte) 34, actor);
			}
			method100(actor);
			method101(actor);
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("29397, " + actor + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method97(Actor actor, boolean bool) {
		do {
			try {
				int i = actor.anInt1567 - Client.currentCycle;
				int i_601_ = actor.anInt1563 * 128 + actor.boundaryDimension * 64;
				int i_602_ = actor.anInt1565 * 128 + actor.boundaryDimension * 64;
				actor.xWithBoundary += (i_601_ - actor.xWithBoundary) / i;
				if (bool) {
					actor.yWithBoundary += (i_602_ - actor.yWithBoundary) / i;
					actor.anInt1523 = 0;
					if (actor.anInt1569 == 0) {
						actor.anInt1530 = 1024;
					}
					if (actor.anInt1569 == 1) {
						actor.anInt1530 = 1536;
					}
					if (actor.anInt1569 == 2) {
						actor.anInt1530 = 0;
					}
					if (actor.anInt1569 != 3) {
						break;
					}
					actor.anInt1530 = 512;
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("99888, " + actor + ", " + bool + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void method98(Actor actor, byte b) {
		do {
			try {
				if (actor.anInt1568 == Client.currentCycle
						|| actor.animation == -1
						|| actor.aniomationDelay != 0
						|| actor.anInt1548 + 1 > AnimationSequence.cache[actor.animation]
								.getFrameLength(actor.anInt1547)) {
					int i = actor.anInt1568 - actor.anInt1567;
					int i_603_ = Client.currentCycle - actor.anInt1567;
					int i_604_ = actor.anInt1563 * 128 + actor.boundaryDimension * 64;
					int i_605_ = actor.anInt1565 * 128 + actor.boundaryDimension * 64;
					int i_606_ = actor.anInt1564 * 128 + actor.boundaryDimension * 64;
					int i_607_ = actor.anInt1566 * 128 + actor.boundaryDimension * 64;
					actor.xWithBoundary = (i_604_ * (i - i_603_) + i_606_ * i_603_) / i;
					actor.yWithBoundary = (i_605_ * (i - i_603_) + i_607_ * i_603_) / i;
				}
				actor.anInt1523 = 0;
				if (actor.anInt1569 == 0) {
					actor.anInt1530 = 1024;
				}
				if (actor.anInt1569 == 1) {
					actor.anInt1530 = 1536;
				}
				if (actor.anInt1569 == 2) {
					actor.anInt1530 = 0;
				}
				if (actor.anInt1569 == 3) {
					actor.anInt1530 = 512;
				}
				actor.anInt1572 = actor.anInt1530;
				if (b == aByte1037) {
					break;
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("10794, " + actor + ", " + b + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void method99(byte b, Actor actor) {
		do {
			try {
				actor.anInt1537 = actor.standAnimationId;
				if (actor.pathLength == 0) {
					actor.anInt1523 = 0;
				} else {
					if (actor.animation != -1 && actor.aniomationDelay == 0) {
						AnimationSequence animationsequence = AnimationSequence.cache[actor.animation];
						if (actor.anInt1562 > 0 && animationsequence.anInt61 == 0) {
							actor.anInt1523++;
							break;
						}
						if (actor.anInt1562 <= 0 && animationsequence.priority == 0) {
							actor.anInt1523++;
							break;
						}
					}
					int i = actor.xWithBoundary;
					int i_608_ = actor.yWithBoundary;
					int i_609_ = actor.pathX[actor.pathLength - 1] * 128 + actor.boundaryDimension * 64;
					int i_610_ = actor.pathY[actor.pathLength - 1] * 128 + actor.boundaryDimension * 64;
					if (i_609_ - i > 256 || i_609_ - i < -256 || i_610_ - i_608_ > 256 || i_610_ - i_608_ < -256) {
						actor.xWithBoundary = i_609_;
						actor.yWithBoundary = i_610_;
					} else {
						if (i < i_609_) {
							if (i_608_ < i_610_) {
								actor.anInt1530 = 1280;
							} else if (i_608_ > i_610_) {
								actor.anInt1530 = 1792;
							} else {
								actor.anInt1530 = 1536;
							}
						} else if (i > i_609_) {
							if (i_608_ < i_610_) {
								actor.anInt1530 = 768;
							} else if (i_608_ > i_610_) {
								actor.anInt1530 = 256;
							} else {
								actor.anInt1530 = 512;
							}
						} else if (i_608_ < i_610_) {
							actor.anInt1530 = 1024;
						} else {
							actor.anInt1530 = 0;
						}
						int i_611_ = actor.anInt1530 - actor.anInt1572 & 0x7ff;
						if (i_611_ > 1024) {
							i_611_ -= 2048;
						}
						int i_612_ = actor.turnAroundAnimationId;
						if (i_611_ >= -256 && i_611_ <= 256) {
							i_612_ = actor.walkAnimationId;
						} else if (i_611_ >= 256 && i_611_ < 768) {
							i_612_ = actor.turnLeftAnimationId;
						} else if (i_611_ >= -768 && i_611_ <= -256) {
							i_612_ = actor.turnRightAnimationId;
						}
						if (i_612_ == -1) {
							i_612_ = actor.walkAnimationId;
						}
						actor.anInt1537 = i_612_;
						int i_613_ = 4;
						if (actor.anInt1572 != actor.anInt1530 && actor.interactingEntity == -1 && actor.anInt1524 != 0) {
							i_613_ = 2;
						}
						if (actor.pathLength > 2) {
							i_613_ = 6;
						}
						if (actor.pathLength > 3) {
							i_613_ = 8;
						}
						if (actor.anInt1523 > 0 && actor.pathLength > 1) {
							i_613_ = 8;
							actor.anInt1523--;
						}
						if (actor.pathRun[actor.pathLength - 1]) {
							i_613_ <<= 1;
						}
						if (i_613_ >= 8 && actor.anInt1537 == actor.walkAnimationId && actor.runAnimationId != -1) {
							actor.anInt1537 = actor.runAnimationId;
						}
						if (i < i_609_) {
							actor.xWithBoundary += i_613_;
							if (actor.xWithBoundary > i_609_) {
								actor.xWithBoundary = i_609_;
							}
						} else if (i > i_609_) {
							actor.xWithBoundary -= i_613_;
							if (actor.xWithBoundary < i_609_) {
								actor.xWithBoundary = i_609_;
							}
						}
						if (i_608_ < i_610_) {
							actor.yWithBoundary += i_613_;
							if (actor.yWithBoundary > i_610_) {
								actor.yWithBoundary = i_610_;
							}
						} else if (i_608_ > i_610_) {
							actor.yWithBoundary -= i_613_;
							if (actor.yWithBoundary < i_610_) {
								actor.yWithBoundary = i_610_;
							}
						}
						if (actor.xWithBoundary != i_609_ || actor.yWithBoundary != i_610_) {
							break;
						}
						actor.pathLength--;
						if (actor.anInt1562 <= 0) {
							break;
						}
						actor.anInt1562--;
					}
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("41887, " + b + ", " + actor + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void method100(Actor actor) {
		do {
			try {
				if (actor.anInt1524 != 0) {
					if (actor.interactingEntity != -1 && actor.interactingEntity < 32768) {
						Npc npc = actors[actor.interactingEntity];
						if (npc != null) {
							int i_614_ = actor.xWithBoundary - npc.xWithBoundary;
							int i_615_ = actor.yWithBoundary - npc.yWithBoundary;
							if (i_614_ != 0 || i_615_ != 0) {
								actor.anInt1530 = (int) (Math.atan2(i_614_, i_615_) * 325.949) & 0x7ff;
							}
						}
					}
					if (actor.interactingEntity >= 32768) {
						int i_616_ = actor.interactingEntity - 32768;
						if (i_616_ == anInt909) {
							i_616_ = localPlayerIndex;
						}
						Player player = players[i_616_];
						if (player != null) {
							int i_617_ = actor.xWithBoundary - player.xWithBoundary;
							int i_618_ = actor.yWithBoundary - player.yWithBoundary;
							if (i_617_ != 0 || i_618_ != 0) {
								actor.anInt1530 = (int) (Math.atan2(i_617_, i_618_) * 325.949) & 0x7ff;
							}
						}
					}
					if ((actor.faceTowardX != 0 || actor.faceTowardY != 0)
							&& (actor.pathLength == 0 || actor.anInt1523 > 0)) {
						int i_619_ = actor.xWithBoundary
								- (actor.faceTowardX - regionAbsoluteBaseX - regionAbsoluteBaseX) * 64;
						int i_620_ = actor.yWithBoundary
								- (actor.faceTowardY - regionAbsoluteBaseY - regionAbsoluteBaseY) * 64;
						if (i_619_ != 0 || i_620_ != 0) {
							actor.anInt1530 = (int) (Math.atan2(i_619_, i_620_) * 325.949) & 0x7ff;
						}
						actor.faceTowardX = 0;
						actor.faceTowardY = 0;
					}
					int i_621_ = actor.anInt1530 - actor.anInt1572 & 0x7ff;
					if (i_621_ == 0) {
						break;
					}
					if (i_621_ < actor.anInt1524 || i_621_ > 2048 - actor.anInt1524) {
						actor.anInt1572 = actor.anInt1530;
					} else if (i_621_ > 1024) {
						actor.anInt1572 -= actor.anInt1524;
					} else {
						actor.anInt1572 += actor.anInt1524;
					}
					actor.anInt1572 &= 0x7ff;
					if (actor.anInt1537 != actor.standAnimationId || actor.anInt1572 == actor.anInt1530) {
						break;
					}
					if (actor.standTurnAnimationId != -1) {
						actor.anInt1537 = actor.standTurnAnimationId;
					} else {
						actor.anInt1537 = actor.walkAnimationId;
					}
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("73745, " + actor + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void method101(Actor actor) {
		do {
			try {
				actor.aBoolean1561 = false;
				if (actor.anInt1537 != -1) {
					AnimationSequence animationsequence = AnimationSequence.cache[actor.anInt1537];
					actor.anInt1539++;
					if (actor.anInt1538 < animationsequence.frameCount
							&& actor.anInt1539 > animationsequence.getFrameLength(actor.anInt1538)) {
						actor.anInt1539 = 0;
						actor.anInt1538++;
					}
					if (actor.anInt1538 >= animationsequence.frameCount) {
						actor.anInt1539 = 0;
						actor.anInt1538 = 0;
					}
				}
				if (actor.spotAnimationId != -1 && Client.currentCycle >= actor.spotAnimationEndCycle) {
					if (actor.currentAnimationFrame < 0) {
						actor.currentAnimationFrame = 0;
					}
					AnimationSequence animationsequence = SpotAnimation.cache[actor.spotAnimationId].sequences;
					for (actor.anInt1542++; actor.currentAnimationFrame < animationsequence.frameCount
							&& actor.anInt1542 > animationsequence.getFrameLength(actor.currentAnimationFrame); actor.currentAnimationFrame++) {
						actor.anInt1542 -= animationsequence.getFrameLength(actor.currentAnimationFrame);
					}
					if (actor.currentAnimationFrame >= animationsequence.frameCount
							&& (actor.currentAnimationFrame < 0 || actor.currentAnimationFrame >= animationsequence.frameCount)) {
						actor.spotAnimationId = -1;
					}
				}
				if (actor.animation != -1 && actor.aniomationDelay <= 1) {
					AnimationSequence animationsequence = AnimationSequence.cache[actor.animation];
					if (animationsequence.anInt61 == 1 && actor.anInt1562 > 0 && actor.anInt1567 <= Client.currentCycle
							&& actor.anInt1568 < Client.currentCycle) {
						actor.aniomationDelay = 1;
						break;
					}
				}
				if (actor.animation != -1 && actor.aniomationDelay == 0) {
					AnimationSequence animationsequence = AnimationSequence.cache[actor.animation];
					for (actor.anInt1548++; actor.anInt1547 < animationsequence.frameCount
							&& actor.anInt1548 > animationsequence.getFrameLength(actor.anInt1547); actor.anInt1547++) {
						actor.anInt1548 -= animationsequence.getFrameLength(actor.anInt1547);
					}
					if (actor.anInt1547 >= animationsequence.frameCount) {
						actor.anInt1547 -= animationsequence.frameStep;
						actor.anInt1550++;
						if (actor.anInt1550 >= animationsequence.anInt60) {
							actor.animation = -1;
						}
						if (actor.anInt1547 < 0 || actor.anInt1547 >= animationsequence.frameCount) {
							actor.animation = -1;
						}
					}
					actor.aBoolean1561 = animationsequence.aBoolean56;
				}
				if (actor.aniomationDelay <= 0) {
					break;
				}
				actor.aniomationDelay--;
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("56331, " + actor + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void processRendering() {
		if (drawGameframe) {
			drawGameframe = false;
			aProducingGraphicsBuffer928.drawGraphics(0, 4, gameGraphics);
			aProducingGraphicsBuffer929.drawGraphics(0, 357, gameGraphics);
			aProducingGraphicsBuffer930.drawGraphics(722, 4, gameGraphics);
			aProducingGraphicsBuffer931.drawGraphics(743, 205, gameGraphics);
			aProducingGraphicsBuffer932.drawGraphics(0, 0, gameGraphics);
			aProducingGraphicsBuffer933.drawGraphics(516, 4, gameGraphics);
			aProducingGraphicsBuffer934.drawGraphics(516, 205, gameGraphics);
			aProducingGraphicsBuffer935.drawGraphics(496, 357, gameGraphics);
			aProducingGraphicsBuffer936.drawGraphics(0, 338, gameGraphics);
			drawTabArea = true;
			aBoolean1248 = true;
			aBoolean1128 = true;
			aBoolean1258 = true;
			if (anInt1048 != 2) {
				currentSceneBuffer.drawGraphics(4, 4, gameGraphics);
				aProducingGraphicsBuffer1189.drawGraphics(550, 4, gameGraphics);
			}
		}
		if (anInt1048 == 2) {
			method146((byte) 1);
		}
		if (actionMenuOpen && anInt973 == 1) {
			drawTabArea = true;
		}
		if (anInt1214 != -1) {
			boolean bool_622_ = method119(anInt970, anInt1214);
			if (bool_622_) {
				drawTabArea = true;
			}
		}
		if (anInt1271 == 2) {
			drawTabArea = true;
		}
		if (anInt1111 == 2) {
			drawTabArea = true;
		}
		if (drawTabArea) {
			renderTab();
			drawTabArea = false;
		}
		if (chatboxWidgetIndex == -1) {
			aWidget1084.scrollPosition = anInt1236 - anInt1114 - 77;
			if (mouseEventX > 448 && mouseEventX < 560 && mouseEventY > 332) {
				method65(463, 77, mouseEventX - 17, mouseEventY - 357, aWidget1084, 0, false, anInt1236, 0);
			}
			int i = anInt1236 - 77 - aWidget1084.scrollPosition;
			if (i < 0) {
				i = 0;
			}
			if (i > anInt1236 - 77) {
				i = anInt1236 - 77;
			}
			if (anInt1114 != i) {
				anInt1114 = i;
				aBoolean1248 = true;
			}
		}
		if (chatboxWidgetIndex != -1) {
			boolean bool_623_ = method119(anInt970, chatboxWidgetIndex);
			if (bool_623_) {
				aBoolean1248 = true;
			}
		}
		if (anInt1271 == 3) {
			aBoolean1248 = true;
		}
		if (anInt1111 == 3) {
			aBoolean1248 = true;
		}
		if (aString869 != null) {
			aBoolean1248 = true;
		}
		if (actionMenuOpen && anInt973 == 2) {
			aBoolean1248 = true;
		}
		if (aBoolean1248) {
			method18();
			aBoolean1248 = false;
		}
		if (anInt1048 == 2) {
			method126();
			aProducingGraphicsBuffer1189.drawGraphics(550, 4, gameGraphics);
		}
		if (flashingSidebar != -1) {
			aBoolean1128 = true;
		}
		if (aBoolean1128) {
			if (flashingSidebar != -1 && flashingSidebar == currentTabIndex) {
				flashingSidebar = -1;
				outBuffer.putOpcode(120);
				outBuffer.put(currentTabIndex);
			}
			aBoolean1128 = false;
			aProducingGraphicsBuffer1150.createRasterizer();
			anIndexedImage1054.drawImage(0, 0);
			if (anInt1214 == -1) {
				if (tabWidgetIndexes[currentTabIndex] != -1) {
					if (currentTabIndex == 0) {
						anIndexedImage1168.drawImage(22, 10);
					}
					if (currentTabIndex == 1) {
						anIndexedImage1169.drawImage(54, 8);
					}
					if (currentTabIndex == 2) {
						anIndexedImage1169.drawImage(82, 8);
					}
					if (currentTabIndex == 3) {
						anIndexedImage1170.drawImage(110, 8);
					}
					if (currentTabIndex == 4) {
						anIndexedImage1172.drawImage(153, 8);
					}
					if (currentTabIndex == 5) {
						anIndexedImage1172.drawImage(181, 8);
					}
					if (currentTabIndex == 6) {
						anIndexedImage1171.drawImage(209, 9);
					}
				}
				if (tabWidgetIndexes[0] != -1 && (flashingSidebar != 0 || Client.currentCycle % 20 < 10)) {
					anIndexedImageArray972[0].drawImage(29, 13);
				}
				if (tabWidgetIndexes[1] != -1 && (flashingSidebar != 1 || Client.currentCycle % 20 < 10)) {
					anIndexedImageArray972[1].drawImage(53, 11);
				}
				if (tabWidgetIndexes[2] != -1 && (flashingSidebar != 2 || Client.currentCycle % 20 < 10)) {
					anIndexedImageArray972[2].drawImage(82, 11);
				}
				if (tabWidgetIndexes[3] != -1 && (flashingSidebar != 3 || Client.currentCycle % 20 < 10)) {
					anIndexedImageArray972[3].drawImage(115, 12);
				}
				if (tabWidgetIndexes[4] != -1 && (flashingSidebar != 4 || Client.currentCycle % 20 < 10)) {
					anIndexedImageArray972[4].drawImage(153, 13);
				}
				if (tabWidgetIndexes[5] != -1 && (flashingSidebar != 5 || Client.currentCycle % 20 < 10)) {
					anIndexedImageArray972[5].drawImage(180, 11);
				}
				if (tabWidgetIndexes[6] != -1 && (flashingSidebar != 6 || Client.currentCycle % 20 < 10)) {
					anIndexedImageArray972[6].drawImage(208, 13);
				}
			}
			aProducingGraphicsBuffer1150.drawGraphics(516, 160, gameGraphics);
			aProducingGraphicsBuffer1149.createRasterizer();
			anIndexedImage1053.drawImage(0, 0);
			if (anInt1214 == -1) {
				if (tabWidgetIndexes[currentTabIndex] != -1) {
					if (currentTabIndex == 7) {
						anIndexedImage890.drawImage(42, 0);
					}
					if (currentTabIndex == 8) {
						anIndexedImage891.drawImage(74, 0);
					}
					if (currentTabIndex == 9) {
						anIndexedImage891.drawImage(102, 0);
					}
					if (currentTabIndex == 10) {
						anIndexedImage892.drawImage(130, 1);
					}
					if (currentTabIndex == 11) {
						anIndexedImage894.drawImage(173, 0);
					}
					if (currentTabIndex == 12) {
						anIndexedImage894.drawImage(201, 0);
					}
					if (currentTabIndex == 13) {
						anIndexedImage893.drawImage(229, 0);
					}
				}
				if (tabWidgetIndexes[8] != -1 && (flashingSidebar != 8 || Client.currentCycle % 20 < 10)) {
					anIndexedImageArray972[7].drawImage(74, 2);
				}
				if (tabWidgetIndexes[9] != -1 && (flashingSidebar != 9 || Client.currentCycle % 20 < 10)) {
					anIndexedImageArray972[8].drawImage(102, 3);
				}
				if (tabWidgetIndexes[10] != -1 && (flashingSidebar != 10 || Client.currentCycle % 20 < 10)) {
					anIndexedImageArray972[9].drawImage(137, 4);
				}
				if (tabWidgetIndexes[11] != -1 && (flashingSidebar != 11 || Client.currentCycle % 20 < 10)) {
					anIndexedImageArray972[10].drawImage(174, 2);
				}
				if (tabWidgetIndexes[12] != -1 && (flashingSidebar != 12 || Client.currentCycle % 20 < 10)) {
					anIndexedImageArray972[11].drawImage(201, 2);
				}
				if (tabWidgetIndexes[13] != -1 && (flashingSidebar != 13 || Client.currentCycle % 20 < 10)) {
					anIndexedImageArray972[12].drawImage(226, 2);
				}
			}
			aProducingGraphicsBuffer1149.drawGraphics(496, 466, gameGraphics);
			currentSceneBuffer.createRasterizer();
		}
		if (aBoolean1258) {
			aBoolean1258 = false;
			aProducingGraphicsBuffer1148.createRasterizer();
			anIndexedImage1052.drawImage(0, 0);
			fontNormal.drawString("Public chat", 55, 28, 0xFFFFFF, true);
			if (publicChatSetting == 0) {
				fontNormal.drawString("On", 55, 41, 65280, true);
			}
			if (publicChatSetting == 1) {
				fontNormal.drawString("Friends", 55, 41, 0xFFFF00, true);
			}
			if (publicChatSetting == 2) {
				fontNormal.drawString("Off", 55, 41, 16711680, true);
			}
			if (publicChatSetting == 3) {
				fontNormal.drawString("Hide", 55, 41, 65535, true);
			}
			fontNormal.drawString("Private chat", 184, 28, 0xFFFFFF, true);
			if (privateChatSetting == 0) {
				fontNormal.drawString("On", 184, 41, 65280, true);
			}
			if (privateChatSetting == 1) {
				fontNormal.drawString("Friends", 184, 41, 0xFFFF00, true);
			}
			if (privateChatSetting == 2) {
				fontNormal.drawString("Off", 184, 41, 16711680, true);
			}
			fontNormal.drawString("Trade/compete", 324, 28, 0xFFFFFF, true);
			if (tradeSetting == 0) {
				fontNormal.drawString("On", 324, 41, 65280, true);
			}
			if (tradeSetting == 1) {
				fontNormal.drawString("Friends", 324, 41, 0xFFFF00, true);
			}
			if (tradeSetting == 2) {
				fontNormal.drawString("Off", 324, 41, 16711680, true);
			}
			fontNormal.drawString("Report abuse", 458, 33, 0xFFFFFF, true);
			aProducingGraphicsBuffer1148.drawGraphics(0, 453, gameGraphics);
			currentSceneBuffer.createRasterizer();
		}
		anInt970 = 0;
	}

	public final boolean method103(Widget widget, boolean bool) {
		try {
			int i = widget.contentType;
			if (bool) {
				startup();
			}
			if (i >= 1 && i <= 200 || i >= 701 && i <= 900) {
				if (i >= 801) {
					i -= 701;
				} else if (i >= 701) {
					i -= 601;
				} else if (i >= 101) {
					i -= 101;
				} else {
					i--;
				}
				menuActionNames[menuActionRow] = "Remove @whi@" + friendsListNames[i];
				menuActionIndexes[menuActionRow] = 792;
				menuActionRow++;
				menuActionNames[menuActionRow] = "Message @whi@" + friendsListNames[i];
				menuActionIndexes[menuActionRow] = 639;
				menuActionRow++;
				return true;
			}
			if (i >= 401 && i <= 500) {
				menuActionNames[menuActionRow] = "Remove @whi@" + widget.disabledText;
				menuActionIndexes[menuActionRow] = 322;
				menuActionRow++;
				return true;
			}
			return false;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("96874, " + widget + ", " + bool + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method104(boolean bool) {
		try {
			AnimableObject animableobject = (AnimableObject) aLinkedList1081.getBack();
			loggedIn &= bool;
			for (/**/; animableobject != null; animableobject = (AnimableObject) aLinkedList1081.getPrevious()) {
				if (animableobject.plane != currentSceneIndex || animableobject.transformCompleted) {
					animableobject.remove();
				} else if (Client.currentCycle >= animableobject.loopCycle) {
					animableobject.nextFrame(anInt970);
					if (animableobject.transformCompleted) {
						animableobject.remove();
					} else {
						currentScene.method507(animableobject.plane, 0, (byte) 6, animableobject.z, -1,
								animableobject.y, 60, animableobject.x, animableobject, false);
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("28956, " + bool + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method105(int i_624_, int i_625_, Widget widget, int i_626_) {
		try {
			if (widget.type == 0
					&& widget.children != null
					&& (!widget.hiddenUntilHovered || anInt1051 == widget.index || anInt1073 == widget.index || anInt1064 == widget.index)) {
				int i_627_ = Rasterizer.topX;
				int i_628_ = Rasterizer.topY;
				int i_629_ = Rasterizer.bottomX;
				int i_630_ = Rasterizer.bottomY;
				Rasterizer.setCoordinates(i_625_, i_626_, i_625_ + widget.width, i_626_ + widget.height);
				int i_631_ = widget.children.length;
				for (int i_632_ = 0; i_632_ < i_631_; i_632_++) {
					int i_633_ = widget.childrenX[i_632_] + i_625_;
					int i_634_ = widget.childrenY[i_632_] + i_626_ - i_624_;
					Widget widget_635_ = Widget.cache[widget.children[i_632_]];
					i_633_ += widget_635_.x;
					i_634_ += widget_635_.y;
					if (widget_635_.contentType > 0) {
						method75(widget_635_);
					}
					if (widget_635_.type == 0) {
						if (widget_635_.scrollPosition > widget_635_.scrollLimit - widget_635_.height) {
							widget_635_.scrollPosition = widget_635_.scrollLimit - widget_635_.height;
						}
						if (widget_635_.scrollPosition < 0) {
							widget_635_.scrollPosition = 0;
						}
						method105(widget_635_.scrollPosition, i_633_, widget_635_, i_634_);
						if (widget_635_.scrollLimit > widget_635_.height) {
							method30(widget_635_.height, widget_635_.scrollPosition, i_634_,
									i_633_ + widget_635_.width, widget_635_.scrollLimit);
						}
					} else if (widget_635_.type != 1) {
						if (widget_635_.type == 2) {
							int i_636_ = 0;
							for (int i_637_ = 0; i_637_ < widget_635_.height; i_637_++) {
								for (int i_638_ = 0; i_638_ < widget_635_.width; i_638_++) {
									int i_639_ = i_633_ + i_638_ * (32 + widget_635_.itemSpritePadsX);
									int i_640_ = i_634_ + i_637_ * (32 + widget_635_.itemSpritePadsY);
									if (i_636_ < 20) {
										i_639_ += widget_635_.imageX[i_636_];
										i_640_ += widget_635_.imageY[i_636_];
									}
									if (widget_635_.items[i_636_] > 0) {
										int i_641_ = 0;
										int i_642_ = 0;
										int i_643_ = widget_635_.items[i_636_] - 1;
										if (i_639_ > Rasterizer.topX - 32 && i_639_ < Rasterizer.bottomX
												&& i_640_ > Rasterizer.topY - 32 && i_640_ < Rasterizer.bottomY
												|| anInt1111 != 0 && anInt1110 == i_636_) {
											int i_644_ = 0;
											if (anInt1307 == 1 && anInt1308 == i_636_ && anInt1309 == widget_635_.index) {
												i_644_ = 0xFFFFFF;
											}
											ImageRGB imagergb = ItemDefinition.getSprite(i_643_,
													widget_635_.itemAmounts[i_636_], i_644_);
											if (imagergb != null) {
												if (anInt1111 != 0 && anInt1110 == i_636_
														&& anInt1109 == widget_635_.index) {
													i_641_ = mouseEventX - anInt1112;
													i_642_ = mouseEventY - anInt1113;
													if (i_641_ < 5 && i_641_ > -5) {
														i_641_ = 0;
													}
													if (i_642_ < 5 && i_642_ > -5) {
														i_642_ = 0;
													}
													if (anInt1014 < 5) {
														i_641_ = 0;
														i_642_ = 0;
													}
													imagergb.drawImageAlpha(i_639_ + i_641_, i_640_ + i_642_, 128);
													if (i_640_ + i_642_ < Rasterizer.topY && widget.scrollPosition > 0) {
														int i_645_ = anInt970 * (Rasterizer.topY - i_640_ - i_642_) / 3;
														if (i_645_ > anInt970 * 10) {
															i_645_ = anInt970 * 10;
														}
														if (i_645_ > widget.scrollPosition) {
															i_645_ = widget.scrollPosition;
														}
														widget.scrollPosition -= i_645_;
														anInt1113 += i_645_;
													}
													if (i_640_ + i_642_ + 32 > Rasterizer.bottomY
															&& widget.scrollPosition < widget.scrollLimit
																	- widget.height) {
														int i_646_ = anInt970
																* (i_640_ + i_642_ + 32 - Rasterizer.bottomY) / 3;
														if (i_646_ > anInt970 * 10) {
															i_646_ = anInt970 * 10;
														}
														if (i_646_ > widget.scrollLimit - widget.height
																- widget.scrollPosition) {
															i_646_ = widget.scrollLimit - widget.height
																	- widget.scrollPosition;
														}
														widget.scrollPosition += i_646_;
														anInt1113 -= i_646_;
													}
												} else if (anInt1271 != 0 && anInt1270 == i_636_
														&& anInt1269 == widget_635_.index) {
													imagergb.drawImageAlpha(i_639_, i_640_, 128);
												} else {
													imagergb.drawImage(i_639_, i_640_);
												}
												if (imagergb.maxWidth == 33 || widget_635_.itemAmounts[i_636_] != 1) {
													int i_647_ = widget_635_.itemAmounts[i_636_];
													fontSmall.drawRegularString(Client.method43(-33245, i_647_), i_639_
															+ 1 + i_641_, i_640_ + 10 + i_642_, 0);
													fontSmall.drawRegularString(Client.method43(-33245, i_647_), i_639_
															+ i_641_, i_640_ + 9 + i_642_, 0xFFFF00);
												}
											}
										}
									} else if (widget_635_.images != null && i_636_ < 20) {
										ImageRGB imagergb = widget_635_.images[i_636_];
										if (imagergb != null) {
											imagergb.drawImage(i_639_, i_640_);
										}
									}
									i_636_++;
								}
							}
						} else if (widget_635_.type == 3) {
							boolean bool = false;
							if (anInt1064 == widget_635_.index || anInt1073 == widget_635_.index
									|| anInt1051 == widget_635_.index) {
								bool = true;
							}
							int i_648_;
							if (method131(widget_635_)) {
								i_648_ = widget_635_.enabledColor;
								if (bool && widget_635_.enabledHoveredColor != 0) {
									i_648_ = widget_635_.enabledHoveredColor;
								}
							} else {
								i_648_ = widget_635_.disabledColor;
								if (bool && widget_635_.disabledHoveredColor != 0) {
									i_648_ = widget_635_.disabledHoveredColor;
								}
							}
							if (widget_635_.alpha == 0) {
								if (widget_635_.filled) {
									Rasterizer.drawFilledRectangle(i_633_, i_634_, widget_635_.width,
											widget_635_.height, i_648_);
								} else {
									Rasterizer.drawUnfilledRectangle(i_633_, i_634_, widget_635_.width,
											widget_635_.height, i_648_);
								}
							} else if (widget_635_.filled) {
								Rasterizer.drawFilledRectangleAlhpa(i_633_, i_634_, widget_635_.width,
										widget_635_.height, i_648_, 256 - (widget_635_.alpha & 0xff));
							} else {
								Rasterizer.drawUnfilledRectangleAlpha(i_633_, i_634_, widget_635_.width,
										widget_635_.height, i_648_, 256 - (widget_635_.alpha & 0xff));
							}
						} else if (widget_635_.type == 4) {
							TypeFace typeface = widget_635_.typeFaces;
							String string = widget_635_.disabledText;
							boolean bool = false;
							if (anInt1064 == widget_635_.index || anInt1073 == widget_635_.index
									|| anInt1051 == widget_635_.index) {
								bool = true;
							}
							int i_649_;
							if (method131(widget_635_)) {
								i_649_ = widget_635_.enabledColor;
								if (bool && widget_635_.enabledHoveredColor != 0) {
									i_649_ = widget_635_.enabledHoveredColor;
								}
								if (widget_635_.enabledText.length() > 0) {
									string = widget_635_.enabledText;
								}
							} else {
								i_649_ = widget_635_.disabledColor;
								if (bool && widget_635_.disabledHoveredColor != 0) {
									i_649_ = widget_635_.disabledHoveredColor;
								}
							}
							if (widget_635_.actionType == 6 && aBoolean1174) {
								string = "Please wait...";
								i_649_ = widget_635_.disabledColor;
							}
							if (Rasterizer.width == 479) {
								if (i_649_ == 0xFFFF00) {
									i_649_ = 255;
								}
								if (i_649_ == 49152) {
									i_649_ = 0xFFFFFF;
								}
							}
							int i_650_ = i_634_ + typeface.characterDefaultHeight;
							while (string.length() > 0) {
								if (string.indexOf("%") != -1) {
									for (;;) {
										int i_651_ = string.indexOf("%1");
										if (i_651_ == -1) {
											break;
										}
										string = string.substring(0, i_651_)
												+ method93(369, parseWidgetOpcode(widget_635_, 0))
												+ string.substring(i_651_ + 2);
									}
									for (;;) {
										int i_652_ = string.indexOf("%2");
										if (i_652_ == -1) {
											break;
										}
										string = string.substring(0, i_652_)
												+ method93(369, parseWidgetOpcode(widget_635_, 1))
												+ string.substring(i_652_ + 2);
									}
									for (;;) {
										int i_653_ = string.indexOf("%3");
										if (i_653_ == -1) {
											break;
										}
										string = string.substring(0, i_653_)
												+ method93(369, parseWidgetOpcode(widget_635_, 2))
												+ string.substring(i_653_ + 2);
									}
									for (;;) {
										int i_654_ = string.indexOf("%4");
										if (i_654_ == -1) {
											break;
										}
										string = string.substring(0, i_654_)
												+ method93(369, parseWidgetOpcode(widget_635_, 3))
												+ string.substring(i_654_ + 2);
									}
									for (;;) {
										int i_655_ = string.indexOf("%5");
										if (i_655_ == -1) {
											break;
										}
										string = string.substring(0, i_655_)
												+ method93(369, parseWidgetOpcode(widget_635_, 4))
												+ string.substring(i_655_ + 2);
									}
								}
								int i_656_ = string.indexOf("\\n");
								String string_657_;
								if (i_656_ != -1) {
									string_657_ = string.substring(0, i_656_);
									string = string.substring(i_656_ + 2);
								} else {
									string_657_ = string;
									string = "";
								}
								if (widget_635_.typeFaceCentered) {
									typeface.drawString(string_657_, i_633_ + widget_635_.width / 2, i_650_, i_649_,
											widget_635_.typeFaceShadowed);
								} else {
									typeface.drawShadowedString(string_657_, i_633_, i_650_,
											widget_635_.typeFaceShadowed, i_649_);
								}
								i_650_ += typeface.characterDefaultHeight;
							}
						} else if (widget_635_.type == 5) {
							ImageRGB imagergb;
							if (method131(widget_635_)) {
								imagergb = widget_635_.enabledImage;
							} else {
								imagergb = widget_635_.disabledImage;
							}
							if (imagergb != null) {
								imagergb.drawImage(i_633_, i_634_);
							}
						} else if (widget_635_.type == 6) {
							int i_658_ = Rasterizer3D.centerX;
							int i_659_ = Rasterizer3D.centerY;
							Rasterizer3D.centerX = i_633_ + widget_635_.width / 2;
							Rasterizer3D.centerY = i_634_ + widget_635_.height / 2;
							int i_660_ = Rasterizer3D.SINE[widget_635_.rotationX] * widget_635_.zoom >> 16;
							int i_661_ = Rasterizer3D.COSINE[widget_635_.rotationX] * widget_635_.zoom >> 16;
							boolean bool = method131(widget_635_);
							int i_662_;
							if (bool) {
								i_662_ = widget_635_.enabledAnimation;
							} else {
								i_662_ = widget_635_.disabledAnimation;
							}
							Model model;
							if (i_662_ == -1) {
								model = widget_635_.getAnimatedModel(-1, -1, bool);
							} else {
								AnimationSequence animationsequence = AnimationSequence.cache[i_662_];
								model = widget_635_.getAnimatedModel(
										animationsequence.frame1Ids[widget_635_.animationFrame],
										animationsequence.frame2Ids[widget_635_.animationFrame], bool);
							}
							if (model != null) {
								model.method430(0, widget_635_.rotationY, 0, widget_635_.rotationX, 0, i_660_, i_661_);
							}
							Rasterizer3D.centerX = i_658_;
							Rasterizer3D.centerY = i_659_;
						} else if (widget_635_.type == 7) {
							TypeFace typeface = widget_635_.typeFaces;
							int i_663_ = 0;
							for (int i_664_ = 0; i_664_ < widget_635_.height; i_664_++) {
								for (int i_665_ = 0; i_665_ < widget_635_.width; i_665_++) {
									if (widget_635_.items[i_663_] > 0) {
										ItemDefinition itemdefinition = ItemDefinition
												.getDefinition(widget_635_.items[i_663_] - 1);
										String string = itemdefinition.name;
										if (itemdefinition.stackable || widget_635_.itemAmounts[i_663_] != 1) {
											string += " x" + Client.formatNumber(widget_635_.itemAmounts[i_663_]);
										}
										int i_666_ = i_633_ + i_665_ * (115 + widget_635_.itemSpritePadsX);
										int i_667_ = i_634_ + i_664_ * (12 + widget_635_.itemSpritePadsY);
										if (widget_635_.typeFaceCentered) {
											typeface.drawString(string, i_666_ + widget_635_.width / 2, i_667_,
													widget_635_.disabledColor, widget_635_.typeFaceShadowed);
										} else {
											typeface.drawShadowedString(string, i_666_, i_667_,
													widget_635_.typeFaceShadowed, widget_635_.disabledColor);
										}
									}
									i_663_++;
								}
							}
						}
					}
				}
				Rasterizer.setCoordinates(i_627_, i_628_, i_629_, i_630_);
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("5217, " + i_624_ + ", " + i_625_ + ", " + widget + ", " + i_626_ + ", "
					+ runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method106(IndexedImage indexedimage, int i) {
		do {
			try {
				int i_668_ = 256;
				if (i >= 0) {
					outBuffer.put(126);
				}
				for (int i_669_ = 0; i_669_ < anIntArray1215.length; i_669_++) {
					anIntArray1215[i_669_] = 0;
				}
				for (int i_670_ = 0; i_670_ < 5000; i_670_++) {
					int i_671_ = (int) (Math.random() * 128.0 * i_668_);
					anIntArray1215[i_671_] = (int) (Math.random() * 256.0);
				}
				for (int i_672_ = 0; i_672_ < 20; i_672_++) {
					for (int i_673_ = 1; i_673_ < i_668_ - 1; i_673_++) {
						for (int i_674_ = 1; i_674_ < 127; i_674_++) {
							int i_675_ = i_674_ + (i_673_ << 7);
							anIntArray1216[i_675_] = (anIntArray1215[i_675_ - 1] + anIntArray1215[i_675_ + 1]
									+ anIntArray1215[i_675_ - 128] + anIntArray1215[i_675_ + 128]) / 4;
						}
					}
					int[] is = anIntArray1215;
					anIntArray1215 = anIntArray1216;
					anIntArray1216 = is;
				}
				if (indexedimage == null) {
					break;
				}
				int i_676_ = 0;
				for (int i_677_ = 0; i_677_ < indexedimage.height; i_677_++) {
					for (int i_678_ = 0; i_678_ < indexedimage.width; i_678_++) {
						if (indexedimage.pixels[i_676_++] != 0) {
							int i_679_ = i_678_ + 16 + indexedimage.xDrawOffset;
							int i_680_ = i_677_ + 16 + indexedimage.yDrawOffset;
							int i_681_ = i_679_ + (i_680_ << 7);
							anIntArray1215[i_681_] = 0;
						}
					}
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("98023, " + indexedimage + ", " + i + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	private final void updatePlayerMask(int mask, int playerIndex, Buffer buffer, Player player) {
		do {
			try {

				if ((mask & 0x400) != 0) {
					player.anInt1563 = buffer.getUnsignedByteS();
					player.anInt1565 = buffer.getUnsignedByteS();
					player.anInt1564 = buffer.getUnsignedByteS();
					player.anInt1566 = buffer.getUnsignedByteS();
					player.anInt1567 = buffer.getUnsignedShortA() + Client.currentCycle;
					player.anInt1568 = buffer.getUnsignedLEShortA() + Client.currentCycle;
					player.anInt1569 = buffer.getUnsignedByteS();
					player.resetPath();
				}

				if ((mask & 0x100) != 0) {
					player.spotAnimationId = buffer.getUnsignedShort();
					int spotAnimationDelay = buffer.getInt();
					player.spotAnimationDelay = spotAnimationDelay >> 16;
					player.spotAnimationEndCycle = Client.currentCycle + (spotAnimationDelay & 0xffff);
					player.currentAnimationFrame = 0;
					player.anInt1542 = 0;
					if (player.spotAnimationEndCycle > Client.currentCycle) {
						player.currentAnimationFrame = -1;
					}
					if (player.spotAnimationId == 65535) {
						player.spotAnimationId = -1;
					}
				}

				if ((mask & 0x8) != 0) {
					int animationId = buffer.getUnsignedShort();
					if (animationId == 65535) {
						animationId = -1;
					}
					int animationDelay = buffer.getUnsignedByteC();
					if (animationId == player.animation && animationId != -1) {
						int i_686_ = AnimationSequence.cache[animationId].anInt63;
						if (i_686_ == 1) {
							player.anInt1547 = 0;
							player.anInt1548 = 0;
							player.aniomationDelay = animationDelay;
							player.anInt1550 = 0;
						}
						if (i_686_ == 2) {
							player.anInt1550 = 0;
						}
					} else if (animationId == -1
							|| player.animation == -1
							|| AnimationSequence.cache[animationId].anInt57 >= AnimationSequence.cache[player.animation].anInt57) {
						player.animation = animationId;
						player.anInt1547 = 0;
						player.anInt1548 = 0;
						player.aniomationDelay = animationDelay;
						player.anInt1550 = 0;
						player.anInt1562 = player.pathLength;
					}
				}

				if ((mask & 0x4) != 0) {
					player.forcedChat = buffer.getString();
					if (player.forcedChat.charAt(0) == '~') {
						player.forcedChat = player.forcedChat.substring(1);
						sendMessage(player.forcedChat, 2, player.playerName);
					} else if (player == Client.localPlayer) {
						sendMessage(player.forcedChat, 2, player.playerName);
					}
					player.chatColor = 0;
					player.chatEffect = 0;
					player.anInt1555 = 150;
				}

				if ((mask & 0x80) != 0) {
					int chatEffects = buffer.getUnsignedShort();
					int playerRights = buffer.getUnsignedByte();
					int chatTextLength = buffer.getUnsignedByteC();
					int originalOffset = buffer.offset;
					if (player.playerName != null && player.visibile) {
						long l = TextUtils.nameToLong(player.playerName);
						boolean bool = false;
						if (playerRights <= 1) {
							for (int i_691_ = 0; i_691_ < ignoreListCount; i_691_++) {
								if (ignoreList[i_691_] == l) {
									bool = true;
									break;
								}
							}
						}
						if (!bool && anInt1276 == 0) {
							try {
								aBuffer859.offset = 0;
								buffer.getBytes(chatTextLength, 0, aBuffer859.payload);
								aBuffer859.offset = 0;
								String forcedChat = ChatEncoder.get(chatTextLength, aBuffer859);
								forcedChat = ChatCensor.censorString(forcedChat);
								player.forcedChat = forcedChat;
								player.chatColor = chatEffects >> 8;
								player.chatEffect = chatEffects & 0xff;
								player.anInt1555 = 150;
								if (playerRights == 2 || playerRights == 3) {
									sendMessage(forcedChat, 1, "@cr2@" + player.playerName);
								} else if (playerRights == 1) {
									sendMessage(forcedChat, 1, "@cr1@" + player.playerName);
								} else {
									sendMessage(forcedChat, 2, player.playerName);
								}
							} catch (Exception exception) {
								SignLink.reportError("cde2");
							}
						}
					}
					buffer.offset = originalOffset + chatTextLength;
				}

				if ((mask & 0x1) != 0) {
					player.interactingEntity = buffer.getUnsignedShort();
					if (player.interactingEntity == 65535) {
						player.interactingEntity = -1;
					}
				}

				if ((mask & 0x10) != 0) {
					int appearanceBufferSize = buffer.getUnsignedByteC();
					byte[] _appearanceBuffer = new byte[appearanceBufferSize];
					Buffer appearanceBuffer = new Buffer(_appearanceBuffer);
					buffer.getBytes(_appearanceBuffer, 0, appearanceBufferSize);
					playerBuffer[playerIndex] = appearanceBuffer;
					player.updatePlayer(appearanceBuffer);
				}

				if ((mask & 0x2) != 0) {
					player.faceTowardX = buffer.getUnsignedShortA();
					player.faceTowardY = buffer.getUnsignedShort();
				}

				if ((mask & 0x20) != 0) {
					int hitDamage = buffer.getUnsignedByte();
					int hitType = buffer.getUnsignedByteA();
					player.updateHits(hitType, hitDamage, Client.currentCycle);
					player.endCycle = Client.currentCycle + 300;
					player.maxHealth = buffer.getUnsignedByteC();
					player.currentHealth = buffer.getUnsignedByte();
				}

				if ((mask & 0x200) != 0) {
					int hitDamage = buffer.getUnsignedByte();
					int hitType = buffer.getUnsignedByteS();
					player.updateHits(hitType, hitDamage, Client.currentCycle);
					player.endCycle = Client.currentCycle + 300;
					player.maxHealth = buffer.getUnsignedByte();
					player.currentHealth = buffer.getUnsignedByteC();
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("50326, " + mask + ", " + playerIndex + ", " + buffer + ", " + player + ", "
						+ runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void method108(int i) {
		do {
			try {
				if (i != 3) {
					opcode = -1;
				}
				try {
					int i_698_ = Client.localPlayer.xWithBoundary + anInt1303;
					int i_699_ = Client.localPlayer.yWithBoundary + anInt1156;
					if (anInt1039 - i_698_ < -500 || anInt1039 - i_698_ > 500 || anInt1040 - i_699_ < -500
							|| anInt1040 - i_699_ > 500) {
						anInt1039 = i_698_;
						anInt1040 = i_699_;
					}
					if (anInt1039 != i_698_) {
						anInt1039 += (i_698_ - anInt1039) / 16;
					}
					if (anInt1040 != i_699_) {
						anInt1040 += (i_699_ - anInt1040) / 16;
					}
					if (keyStatus[1] == 1) {
						anInt1211 += (-24 - anInt1211) / 2;
					} else if (keyStatus[2] == 1) {
						anInt1211 += (24 - anInt1211) / 2;
					} else {
						anInt1211 /= 2;
					}
					if (keyStatus[3] == 1) {
						anInt1212 += (12 - anInt1212) / 2;
					} else if (keyStatus[4] == 1) {
						anInt1212 += (-12 - anInt1212) / 2;
					} else {
						anInt1212 /= 2;
					}
					anInt1210 = anInt1210 + anInt1211 / 2 & 0x7ff;
					anInt1209 += anInt1212 / 2;
					if (anInt1209 < 128) {
						anInt1209 = 128;
					}
					if (anInt1209 > 383) {
						anInt1209 = 383;
					}
					int i_700_ = anInt1039 >> 7;
					int i_701_ = anInt1040 >> 7;
					int i_702_ = method42(currentSceneIndex, anInt1040, true, anInt1039);
					int i_703_ = 0;
					if (i_700_ > 3 && i_701_ > 3 && i_700_ < 100 && i_701_ < 100) {
						for (int i_704_ = i_700_ - 4; i_704_ <= i_700_ + 4; i_704_++) {
							for (int i_705_ = i_701_ - 4; i_705_ <= i_701_ + 4; i_705_++) {
								int i_706_ = currentSceneIndex;
								if (i_706_ < 3 && (currentSceneTileFlags[1][i_704_][i_705_] & 0x2) == 2) {
									i_706_++;
								}
								int i_707_ = i_702_ - anIntArrayArrayArray1239[i_706_][i_704_][i_705_];
								if (i_707_ > i_703_) {
									i_703_ = i_707_;
								}
							}
						}
					}
					Client.anInt1030++;
					if (Client.anInt1030 > 1512) {
						Client.anInt1030 = 0;
						outBuffer.putOpcode(77);
						outBuffer.put(0);
						int i_708_ = outBuffer.offset;
						outBuffer.put((int) (Math.random() * 256.0));
						outBuffer.put(101);
						outBuffer.put(233);
						outBuffer.putShort(45092);
						if ((int) (Math.random() * 2.0) == 0) {
							outBuffer.putShort(35784);
						}
						outBuffer.put((int) (Math.random() * 256.0));
						outBuffer.put(64);
						outBuffer.put(38);
						outBuffer.putShort((int) (Math.random() * 65536.0));
						outBuffer.putShort((int) (Math.random() * 65536.0));
						outBuffer.putSizeByte(outBuffer.offset - i_708_);
					}
					int i_709_ = i_703_ * 192;
					if (i_709_ > 98048) {
						i_709_ = 98048;
					}
					if (i_709_ < 32768) {
						i_709_ = 32768;
					}
					if (i_709_ > anInt1009) {
						anInt1009 += (i_709_ - anInt1009) / 24;
					} else {
						if (i_709_ >= anInt1009) {
							break;
						}
						anInt1009 += (i_709_ - anInt1009) / 80;
					}
				} catch (Exception exception) {
					SignLink.reportError("glfc_ex " + Client.localPlayer.xWithBoundary + ","
							+ Client.localPlayer.yWithBoundary + "," + anInt1039 + "," + anInt1040 + "," + anInt1094
							+ "," + anInt1095 + "," + regionAbsoluteBaseX + "," + regionAbsoluteBaseY);
					throw new RuntimeException("eek");
				}
				break;
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("25141, " + i + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
		} while (false);
	}

	@Override
	public final void repaintGame() {
		if (alreadyLoaded || loadingError || genericLoadingError) {
			method94(-13873);
		} else {
			Client.anInt1086++;
			if (!loggedIn) {
				renderLoginScreen(false);
			} else {
				processRendering();
			}
			anInt1238 = 0;
		}
	}

	public final boolean method109(boolean bool, String string) {
		try {
			if (string == null) {
				return false;
			}
			for (int i = 0; i < friendsListCount; i++) {
				if (string.equalsIgnoreCase(friendsListNames[i])) {
					return true;
				}
			}
			if (bool) {
				outBuffer.put(138);
			}
			if (string.equalsIgnoreCase(Client.localPlayer.playerName)) {
				return true;
			}
			return false;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("24883, " + bool + ", " + string + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public static final String method110(int i, int i_710_, boolean bool) {
		try {
			if (!bool) {
				throw new NullPointerException();
			}
			int i_711_ = i - i_710_;
			if (i_711_ < -9) {
				return "@red@";
			}
			if (i_711_ < -6) {
				return "@or3@";
			}
			if (i_711_ < -3) {
				return "@or2@";
			}
			if (i_711_ < 0) {
				return "@or1@";
			}
			if (i_711_ > 9) {
				return "@gre@";
			}
			if (i_711_ > 6) {
				return "@gr3@";
			}
			if (i_711_ > 3) {
				return "@gr2@";
			}
			if (i_711_ > 0) {
				return "@gr1@";
			}
			return "@yel@";
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("19760, " + i + ", " + i_710_ + ", " + bool + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void setWaveVolume(int i) {
		try {
			SignLink.waveVolume = i;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("83178, " + i + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method112() {
		method76((byte) -13);
		if (anInt942 == 1) {
			anImageRGBArray1175[anInt941 / 100].drawImage(anInt939 - 8 - 4, anInt940 - 8 - 4);
			Client.anInt1167++;
			if (Client.anInt1167 > 67) {
				Client.anInt1167 = 0;
				outBuffer.putOpcode(78);
			}
		}
		if (anInt942 == 2) {
			anImageRGBArray1175[4 + anInt941 / 100].drawImage(anInt939 - 8 - 4, anInt940 - 8 - 4);
		}
		if (walkablewidgetIndex != -1) {
			method119(anInt970, walkablewidgetIndex);
			method105(0, 0, Widget.cache[walkablewidgetIndex], 0);
		}
		if (openWidgetIndex != -1) {
			method119(anInt970, openWidgetIndex);
			method105(0, 0, Widget.cache[openWidgetIndex], 0);
		}
		method70(184);
		if (!actionMenuOpen) {
			processClickingAreas();
			method125();
		} else if (anInt973 == 0) {
			method40();
		}
		if (anInt1080 == 1) {
			anImageRGBArray1120[1].drawImage(472, 296);
		}
		if (Client.debug) {
			int x = 507;
			int y = 20;
			int color = fps > 15 ? 0xFFFF00 : 0xFF0000;
			fontNormal.drawStringAlignRight("FPS: " + fps, x, y, color);
			y += 15;
			Runtime runtime = Runtime.getRuntime();
			int memoryUsage = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			color = 0xFFFF00;
			if (memoryUsage > 0x2000000 && Client.lowMemory) {
				color = 0xFF0000;
			}
			fontNormal.drawStringAlignRight("Mem: " + memoryUsage + "k (" + (memoryUsage / 1024L) + "MB)", x, y,
					0xFFFF00);
			y += 15;
		}
		if (systemUpdateTime != 0) {
			int i_716_ = systemUpdateTime / 50;
			int i_717_ = i_716_ / 60;
			i_716_ %= 60;
			if (i_716_ < 10) {
				fontNormal.drawRegularString("System update in: " + i_717_ + ":0" + i_716_, 4, 329, 0xFFFF00);
			} else {
				fontNormal.drawRegularString("System update in: " + i_717_ + ":" + i_716_, 4, 329, 0xFFFF00);
			}
		}
	}

	public final void method113(long l, int i) {
		try {
			if (l != 0L) {
				if (ignoreListCount >= 100) {
					sendMessage("Your ignore list is full. Max of 100 hit", 0, "");
				} else {
					String string = TextUtils.formatName(TextUtils.longToName(l));
					for (int i_718_ = 0; i_718_ < ignoreListCount; i_718_++) {
						if (ignoreList[i_718_] == l) {
							sendMessage(string + " is already on your ignore list", 0, "");
							return;
						}
					}
					if (i >= 4 && i <= 4) {
						for (int i_719_ = 0; i_719_ < friendsListCount; i_719_++) {
							if (friendsListLongs[i_719_] == l) {
								sendMessage("Please remove " + string + " from your friend list first", 0, "");
								return;
							}
						}
						ignoreList[ignoreListCount++] = l;
						drawTabArea = true;
						outBuffer.putOpcode(133);
						outBuffer.putLong(l);
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("45688, " + l + ", " + i + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method114() {
		try {
			for (int i = -1; i < playerCount; i++) {
				int index;
				if (i == -1) {
					index = localPlayerIndex;
				} else {
					index = anIntArray917[i];
				}
				Player player = players[index];
				if (player != null) {
					method96(player);
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("2450, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	private final void method115() {
		do {
			try {
				if (anInt1048 != 2) {
					break;
				}
				for (SpawnObjectNode node = (SpawnObjectNode) aLinkedList1204.getBack(); node != null; node = (SpawnObjectNode) aLinkedList1204
						.getPrevious()) {
					if (node.anInt1344 > 0) {
						node.anInt1344--;
					}
					if (node.anInt1344 == 0) {
						if (node.id < 0 || Region.method460(node.id, node.anInt1351, 8)) {
							method142(node.y, node.plane, node.face, node.anInt1351, node.x, node.type, node.id, 4);
							node.remove();
						}
					} else {
						if (node.anInt1352 > 0) {
							node.anInt1352--;
						}
						if (node.anInt1352 == 0 && node.x >= 1 && node.y >= 1 && node.x <= 102 && node.y <= 102
								&& (node.anInt1341 < 0 || Region.method460(node.anInt1341, node.anInt1343, 8))) {
							method142(node.y, node.plane, node.anInt1342, node.anInt1343, node.x, node.type,
									node.anInt1341, 4);
							node.anInt1352 = -1;
							if (node.anInt1341 == node.id && node.id == -1) {
								node.remove();
							} else if (node.anInt1341 == node.id && node.anInt1342 == node.face
									&& node.anInt1343 == node.anInt1351) {
								node.remove();
							}
						}
					}
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("99295, " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void processMenuHovering() {
		do {
			try {
				int width = fontBold.getStringEffectWidth("Choose Option");
				for (int i = 0; i < menuActionRow; i++) {
					int actionWidth = fontBold.getStringEffectWidth(menuActionNames[i]);
					if (actionWidth > width) {
						width = actionWidth;
					}
				}
				width += 8;
				int menuHeight = (15 * menuActionRow) + 21;
				if (clickX > 4 && clickY > 4 && clickX < 516 && clickY < 338) {
					int menuX = clickX - 4 - width / 2;
					if (menuX + width > 512) {
						menuX = 512 - width;
					}
					if (menuX < 0) {
						menuX = 0;
					}
					int menuY = clickY - 4;
					if (menuY + menuHeight > 334) {
						menuY = 334 - menuHeight;
					}
					if (menuY < 0) {
						menuY = 0;
					}
					actionMenuOpen = true;
					anInt973 = 0;
					anInt974 = menuX;
					anInt975 = menuY;
					anInt976 = width;
					anInt977 = 15 * menuActionRow + 22;
				}
				if (clickX > 553 && clickY > 205 && clickX < 743 && clickY < 466) {
					int menuX = clickX - 553 - width / 2;
					if (menuX < 0) {
						menuX = 0;
					} else if (menuX + width > 190) {
						menuX = 190 - width;
					}
					int menuY = clickY - 205;
					if (menuY < 0) {
						menuY = 0;
					} else if (menuY + menuHeight > 261) {
						menuY = 261 - menuHeight;
					}
					actionMenuOpen = true;
					anInt973 = 1;
					anInt974 = menuX;
					anInt975 = menuY;
					anInt976 = width;
					anInt977 = 15 * menuActionRow + 22;
				}
				if (clickX <= 17 || clickY <= 357 || clickX >= 496 || clickY >= 453) {
					break;
				}
				int menuX = clickX - 17 - width / 2;
				if (menuX < 0) {
					menuX = 0;
				} else if (menuX + width > 479) {
					menuX = 479 - width;
				}
				int menuY = clickY - 357;
				if (menuY < 0) {
					menuY = 0;
				} else if (menuY + menuHeight > 96) {
					menuY = 96 - menuHeight;
				}
				actionMenuOpen = true;
				anInt973 = 2;
				anInt974 = menuX;
				anInt975 = menuY;
				anInt976 = width;
				anInt977 = 15 * menuActionRow + 22;
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("40223, " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	private final void method117(Buffer buffer) {
		try {
			buffer.initBitAccess();
			opcode = buffer.getUnsignedByte();
			int i_730_ = buffer.getBits(1);
			if (i_730_ != 0) {
				int i_731_ = buffer.getBits(2);
				if (i_731_ == 0) {
					anIntArray919[anInt918++] = localPlayerIndex;
				} else if (i_731_ == 1) {
					int i_732_ = buffer.getBits(3);
					Client.localPlayer.move(i_732_, false);
					int i_733_ = buffer.getBits(1);
					if (i_733_ == 1) {
						anIntArray919[anInt918++] = localPlayerIndex;
					}
				} else if (i_731_ == 2) {
					int i_734_ = buffer.getBits(3);
					Client.localPlayer.move(i_734_, true);
					int i_735_ = buffer.getBits(3);
					Client.localPlayer.move(i_735_, true);
					int i_736_ = buffer.getBits(1);
					if (i_736_ == 1) {
						anIntArray919[anInt918++] = localPlayerIndex;
					}
				} else if (i_731_ == 3) {
					currentSceneIndex = buffer.getBits(2);
					buffer.getBits(1);
					int i_738_ = buffer.getBits(1);
					if (i_738_ == 1) {
						anIntArray919[anInt918++] = localPlayerIndex;
					}
					int i_739_ = buffer.getBits(7);
					int i_740_ = buffer.getBits(7);
					Client.localPlayer.setPosition(i_740_, i_739_);
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("453, " + buffer + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method118() {
		try {
			aBoolean856 = false;
			while (aBoolean987) {
				aBoolean856 = false;
				try {
					Thread.sleep(50L);
				} catch (Exception exception) {
					/* empty */
				}
			}
			titleboxImage = null;
			titleboxButtonImage = null;
			titleFlameEmblem = null;
			anIntArray875 = null;
			anIntArray876 = null;
			anIntArray877 = null;
			anIntArray878 = null;
			anIntArray1215 = null;
			anIntArray1216 = null;
			anIntArray853 = null;
			anIntArray854 = null;
			anImageRGB1226 = null;
			anImageRGB1227 = null;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("81448, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final boolean method119(int i, int widgetIndex) {
		try {
			boolean bool_742_ = false;
			Widget widget = Widget.cache[widgetIndex];
			for (int i_743_ = 0; i_743_ < widget.children.length; i_743_++) {
				if (widget.children[i_743_] == -1) {
					break;
				}
				Widget widget_744_ = Widget.cache[widget.children[i_743_]];
				if (widget_744_.type == 1) {
					bool_742_ |= method119(i, widget_744_.index);
				}
				if (widget_744_.type == 6
						&& (widget_744_.disabledAnimation != -1 || widget_744_.enabledAnimation != -1)) {
					boolean bool_745_ = method131(widget_744_);
					int i_746_;
					if (bool_745_) {
						i_746_ = widget_744_.enabledAnimation;
					} else {
						i_746_ = widget_744_.disabledAnimation;
					}
					if (i_746_ != -1) {
						AnimationSequence animationsequence = AnimationSequence.cache[i_746_];
						widget_744_.animationDuration += i;
						while (widget_744_.animationDuration > animationsequence
								.getFrameLength(widget_744_.animationFrame)) {
							widget_744_.animationDuration -= animationsequence
									.getFrameLength(widget_744_.animationFrame) + 1;
							widget_744_.animationFrame++;
							if (widget_744_.animationFrame >= animationsequence.frameCount) {
								widget_744_.animationFrame -= animationsequence.frameStep;
								if (widget_744_.animationFrame < 0
										|| widget_744_.animationFrame >= animationsequence.frameCount) {
									widget_744_.animationFrame = 0;
								}
							}
							bool_742_ = true;
						}
					}
				}
			}
			return bool_742_;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("91882, " + i + ", " + widgetIndex + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final int method120() {
		try {
			int i_747_ = 3;
			if (anInt886 < 310) {
				int i_748_ = anInt883 >> 7;
				int i_749_ = anInt885 >> 7;
				int i_750_ = Client.localPlayer.xWithBoundary >> 7;
				int i_751_ = Client.localPlayer.yWithBoundary >> 7;
				if ((currentSceneTileFlags[currentSceneIndex][i_748_][i_749_] & 0x4) != 0) {
					i_747_ = currentSceneIndex;
				}
				int i_752_;
				if (i_750_ > i_748_) {
					i_752_ = i_750_ - i_748_;
				} else {
					i_752_ = i_748_ - i_750_;
				}
				int i_753_;
				if (i_751_ > i_749_) {
					i_753_ = i_751_ - i_749_;
				} else {
					i_753_ = i_749_ - i_751_;
				}
				if (i_752_ > i_753_) {
					int i_754_ = i_753_ * 65536 / i_752_;
					int i_755_ = 32768;
					while (i_748_ != i_750_) {
						if (i_748_ < i_750_) {
							i_748_++;
						} else if (i_748_ > i_750_) {
							i_748_--;
						}
						if ((currentSceneTileFlags[currentSceneIndex][i_748_][i_749_] & 0x4) != 0) {
							i_747_ = currentSceneIndex;
						}
						i_755_ += i_754_;
						if (i_755_ >= 65536) {
							i_755_ -= 65536;
							if (i_749_ < i_751_) {
								i_749_++;
							} else if (i_749_ > i_751_) {
								i_749_--;
							}
							if ((currentSceneTileFlags[currentSceneIndex][i_748_][i_749_] & 0x4) != 0) {
								i_747_ = currentSceneIndex;
							}
						}
					}
				} else {
					int i_756_ = i_752_ * 65536 / i_753_;
					int i_757_ = 32768;
					while (i_749_ != i_751_) {
						if (i_749_ < i_751_) {
							i_749_++;
						} else if (i_749_ > i_751_) {
							i_749_--;
						}
						if ((currentSceneTileFlags[currentSceneIndex][i_748_][i_749_] & 0x4) != 0) {
							i_747_ = currentSceneIndex;
						}
						i_757_ += i_756_;
						if (i_757_ >= 65536) {
							i_757_ -= 65536;
							if (i_748_ < i_750_) {
								i_748_++;
							} else if (i_748_ > i_750_) {
								i_748_--;
							}
							if ((currentSceneTileFlags[currentSceneIndex][i_748_][i_749_] & 0x4) != 0) {
								i_747_ = currentSceneIndex;
							}
						}
					}
				}
			}
			if ((currentSceneTileFlags[currentSceneIndex][Client.localPlayer.xWithBoundary >> 7][Client.localPlayer.yWithBoundary >> 7] & 0x4) != 0) {
				i_747_ = currentSceneIndex;
			}
			return i_747_;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("62088, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final int method121(int i) {
		try {
			while (i >= 0) {
				outBuffer.put(21);
			}
			int i_758_ = method42(currentSceneIndex, anInt885, true, anInt883);
			if (i_758_ - anInt884 < 800
					&& (currentSceneTileFlags[currentSceneIndex][anInt883 >> 7][anInt885 >> 7] & 0x4) != 0) {
				return currentSceneIndex;
			}
			return 3;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("3005, " + i + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method122(int i, long l) {
		try {
			if (i != 3) {
				startup();
			}
			if (l != 0L) {
				for (int i_759_ = 0; i_759_ < ignoreListCount; i_759_++) {
					if (ignoreList[i_759_] == l) {
						ignoreListCount--;
						drawTabArea = true;
						for (int i_760_ = i_759_; i_760_ < ignoreListCount; i_760_++) {
							ignoreList[i_760_] = ignoreList[i_760_ + 1];
						}
						outBuffer.putOpcode(74);
						outBuffer.putLong(l);
						break;
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("47229, " + i + ", " + l + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	@Override
	public final String getParameter(String string) {
		if (SignLink.applet != null) {
			return SignLink.applet.getParameter(string);
		}
		return super.getParameter(string);
	}

	public final void setMidiVolume(boolean bool, int volume) {
		do {
			try {
				SignLink.midiVolume = volume;
				if (!bool) {
					break;
				}
				SignLink.nextSongName = "voladjust";
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("30156, " + bool + ", " + volume + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final int parseWidgetOpcode(Widget widget, int widgetId) {
		if (widget.opcodes == null || widgetId >= widget.opcodes.length) {
			return -2;
		}
		try {
			int[] opcodes = widget.opcodes[widgetId];
			int result = 0;
			int counter = 0;
			int type = 0;
			while (true) {
				int opcode = opcodes[counter++];
				int value = 0;
				int tempType = 0;
				if (opcode == 0) {
					return result;
				}
				if (opcode == 1) {
					value = skillLevel[opcodes[counter++]];
				}
				if (opcode == 2) {
					value = skillMaxLevel[opcodes[counter++]];
				}
				if (opcode == 3) {
					value = skillExperience[opcodes[counter++]];
				}
				if (opcode == 4) {
					Widget itemWidget = Widget.cache[opcodes[counter++]];
					int itemId = opcodes[counter++];
					if (itemId >= 0 && itemId < ItemDefinition.itemCount
							&& (!ItemDefinition.getDefinition(itemId).membersOnly || Client.membersWorld)) {
						for (int item = 0; item < itemWidget.items.length; item++) {
							if (itemWidget.items[item] == itemId + 1) {
								value += itemWidget.itemAmounts[item];
							}
						}
					}
				}
				if (opcode == 5) {
					value = settings[opcodes[counter++]];
				}
				if (opcode == 6) {
					value = Client.xpForSkillLevel[skillMaxLevel[opcodes[counter++]] - 1];
				}
				if (opcode == 7) {
					value = settings[opcodes[counter++]] * 100 / 46875;
				}
				if (opcode == 8) {
					value = Client.localPlayer.combatLevel;
				}
				if (opcode == 9) {
					for (int skilll = 0; skilll < SkillConstants.SKILL_COUNT; skilll++) {
						if (SkillConstants.SKILL_TOGGLES[skilll]) {
							value += skillMaxLevel[skilll];
						}
					}
				}
				if (opcode == 10) {
					Widget itemWidget = Widget.cache[opcodes[counter++]];
					int itemId = opcodes[counter++] + 1;
					if (itemId >= 0 && itemId < ItemDefinition.itemCount
							&& (!ItemDefinition.getDefinition(itemId).membersOnly || Client.membersWorld)) {
						for (int item = 0; item < itemWidget.items.length; item++) {
							if (itemWidget.items[item] == itemId) {
								value = 999999999;
								break;
							}
						}
					}
				}
				if (opcode == 11) {
					value = playerEnergy;
				}
				if (opcode == 12) {
					value = playerWeight;
				}
				if (opcode == 13) {
					int setting = settings[opcodes[counter++]];
					int info = opcodes[counter++];
					value = (setting & 1 << info) != 0 ? 1 : 0;
				}
				if (opcode == 14) {
					int i_777_ = opcodes[counter++];
					VarBit varbit = VarBit.cache[i_777_];
					int i_778_ = varbit.configId;
					int i_779_ = varbit.leastSignificantBit;
					int i_780_ = varbit.mostSignificantBit;
					int i_781_ = Client.BITFIELD_MAX_VALUE[i_780_ - i_779_];
					value = settings[i_778_] >> i_779_ & i_781_;
				}
				if (opcode == 15) {
					tempType = 1;
				}
				if (opcode == 16) {
					tempType = 2;
				}
				if (opcode == 17) {
					tempType = 3;
				}
				if (opcode == 18) {
					value = (Client.localPlayer.xWithBoundary >> 7) + regionAbsoluteBaseX;
				}
				if (opcode == 19) {
					value = (Client.localPlayer.yWithBoundary >> 7) + regionAbsoluteBaseY;
				}
				if (opcode == 20) {
					value = opcodes[counter++];
				}
				if (tempType == 0) {
					if (type == 0) {
						result += value;
					}
					if (type == 1) {
						result -= value;
					}
					if (type == 2 && value != 0) {
						result /= value;
					}
					if (type == 3) {
						result *= value;
					}
					type = 0;
				} else {
					type = tempType;
				}
			}
		} catch (Exception exception) {
			return -1;
		}
	}

	public final void method125() {
		do {
			try {
				if (menuActionRow >= 2 || anInt1307 != 0 || anInt1161 != 0) {
					String action;
					if (anInt1307 == 1 && menuActionRow < 2) {
						action = "Use " + aString1311 + " with...";
					} else if (anInt1161 == 1 && menuActionRow < 2) {
						action = aString1164 + "...";
					} else {
						action = menuActionNames[menuActionRow - 1];
					}
					if (menuActionRow > 2) {
						action += "@whi@ / " + (menuActionRow - 2) + " more options";
					}
					fontBold.drawShadowedSeededAlphaString(action, 4, 15, Client.currentCycle / 1000, 0xFFFFFF);
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("86922, " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void method126() {
		try {
			aProducingGraphicsBuffer1189.createRasterizer();
			if (minimapState == 2) {
				byte[] mapbackPixels = minimapBackgroundImage.pixels;
				int[] rasterPixels = Rasterizer.pixels;
				int length = mapbackPixels.length;
				for (int i = 0; i < length; i++) {
					if (mapbackPixels[i] == 0) {
						rasterPixels[i] = 0;
					}
				}
				minimapCompass.shapeImageToPixels(33, anInt1210, anIntArray1082, 256, anIntArray993, 25, 0, 0, 33, 25);
				currentSceneBuffer.createRasterizer();
			} else {
				int mapRotation = anInt1210 + anInt1234 & 0x7ff;
				int mapX = 48 + Client.localPlayer.xWithBoundary / 32;
				int mapY = 464 - Client.localPlayer.yWithBoundary / 32;
				minimapImage.shapeImageToPixels(151, mapRotation, anIntArray1254, 256 + anInt1195, anIntArray1077,
						mapY, 5, 25, 146, mapX);
				minimapCompass.shapeImageToPixels(33, anInt1210, anIntArray1082, 256, anIntArray993, 25, 0, 0, 33, 25);

				for (int i = 0; i < minimapHintCount; i++) {
					mapX = minimapHintX[i] * 4 + 2 - Client.localPlayer.xWithBoundary / 32;
					mapY = minimapHintY[i] * 4 + 2 - Client.localPlayer.yWithBoundary / 32;
					method141(minimapHint[i], mapX, mapY);
				}

				/* Ground Items */
				for (int x = 0; x < 104; x++) {
					for (int y = 0; y < 104; y++) {
						LinkedList groundItems = groundItemNodes[currentSceneIndex][x][y];
						if (groundItems != null) {
							mapX = x * 4 + 2 - Client.localPlayer.xWithBoundary / 32;
							mapY = y * 4 + 2 - Client.localPlayer.yWithBoundary / 32;
							method141(mapdotItem, mapX, mapY);
						}
					}
				}

				/* Actors */
				for (int i = 0; i < actorCount; i++) {
					Npc npc = actors[anIntArray862[i]];
					if (npc != null && npc.isVisibile()) {
						ActorDefinition definition = npc.npcDefinition;
						if (definition.childrenIndexes != null) {
							definition = definition.getChildDefinition();
						}
						if (definition != null && definition.minimapVisible && definition.clickable) {
							mapX = npc.xWithBoundary / 32 - Client.localPlayer.xWithBoundary / 32;
							mapY = npc.yWithBoundary / 32 - Client.localPlayer.yWithBoundary / 32;
							method141(mapdotActor, mapX, mapY);
						}
					}
				}

				/* Players */
				for (int index = 0; index < playerCount; index++) {
					Player player = players[anIntArray917[index]];
					if (player != null && player.isVisibile()) {
						mapX = player.xWithBoundary / 32 - Client.localPlayer.xWithBoundary / 32;
						mapY = player.yWithBoundary / 32 - Client.localPlayer.yWithBoundary / 32;
						boolean isFriend = false;
						long nameLong = TextUtils.nameToLong(player.playerName);
						for (int i_792_ = 0; i_792_ < friendsListCount; i_792_++) {
							if (nameLong == friendsListLongs[i_792_] && friendsListWorlds[i_792_] != 0) {
								isFriend = true;
								break;
							}
						}
						boolean isTeammate = false;
						if (Client.localPlayer.teamIndex != 0 && player.teamIndex != 0
								&& Client.localPlayer.teamIndex == player.teamIndex) {
							isTeammate = true;
						}
						if (isFriend) {
							method141(mapdotFriend, mapX, mapY);
						} else if (isTeammate) {
							method141(mapdotTeammate, mapX, mapY);
						} else {
							method141(mapdotPlayer, mapX, mapY);
						}
					}
				}

				/* Hints */
				if (hintIconType != 0 && Client.currentCycle % 20 < 10) {
					if (hintIconType == 1 && hintIconActorIndex >= 0 && hintIconActorIndex < actors.length) {
						Npc npc = actors[hintIconActorIndex];
						if (npc != null) {
							mapX = npc.xWithBoundary / 32 - Client.localPlayer.xWithBoundary / 32;
							mapY = npc.yWithBoundary / 32 - Client.localPlayer.yWithBoundary / 32;
							method81(anImageRGB896, -760, mapY, mapX);
						}
					}
					if (hintIconType == 2) {
						mapX = (hintIconX - regionAbsoluteBaseX) * 4 + 2 - Client.localPlayer.xWithBoundary / 32;
						mapY = (hintIconY - regionAbsoluteBaseY) * 4 + 2 - Client.localPlayer.yWithBoundary / 32;
						method81(anImageRGB896, -760, mapY, mapX);
					}
					if (hintIconType == 10 && hintIconPlayerIndex >= 0 && hintIconPlayerIndex < players.length) {
						Player player = players[hintIconPlayerIndex];
						if (player != null) {
							mapX = player.xWithBoundary / 32 - Client.localPlayer.xWithBoundary / 32;
							mapY = player.yWithBoundary / 32 - Client.localPlayer.yWithBoundary / 32;
							method81(anImageRGB896, -760, mapY, mapX);
						}
					}
				}

				/* Flag Marker */
				if (destinationX != 0) {
					mapX = destinationX * 4 + 2 - Client.localPlayer.xWithBoundary / 32;
					mapY = destinationY * 4 + 2 - Client.localPlayer.yWithBoundary / 32;
					method141(mapFlagMarker, mapX, mapY);
				}

				Rasterizer.drawFilledRectangle(97, 78, 3, 3, 0xFFFFFF);
				currentSceneBuffer.createRasterizer();
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("83200, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method127(boolean bool, Actor actor, int i) {
		try {
			if (!bool) {
				opcode = inBuffer.getUnsignedByte();
			}
			method128(actor.xWithBoundary, i, anInt900, actor.yWithBoundary);
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("30100, " + bool + ", " + actor + ", " + i + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method128(int i, int i_794_, int i_795_, int i_796_) {
		try {
			if (i < 128 || i_796_ < 128 || i > 13056 || i_796_ > 13056) {
				anInt988 = -1;
				anInt989 = -1;
			} else {
				int i_797_ = method42(currentSceneIndex, i_796_, true, i) - i_794_;
				i -= anInt883;
				i_797_ -= anInt884;
				i_796_ -= anInt885;
				int i_798_ = Model.anIntArray1682[anInt886];
				int i_799_ = Model.anIntArray1683[anInt886];
				int i_800_ = Model.anIntArray1682[anInt887];
				int i_801_ = Model.anIntArray1683[anInt887];
				int i_802_ = i_796_ * i_800_ + i * i_801_ >> 16;
				i_796_ = i_796_ * i_801_ - i * i_800_ >> 16;
				i = i_802_;
				if (i_795_ >= 0) {
					outBuffer.put(27);
				}
				i_802_ = i_797_ * i_799_ - i_796_ * i_798_ >> 16;
				i_796_ = i_797_ * i_798_ + i_796_ * i_799_ >> 16;
				i_797_ = i_802_;
				if (i_796_ >= 50) {
					anInt988 = Rasterizer3D.centerX + (i << 9) / i_796_;
					anInt989 = Rasterizer3D.centerY + (i_797_ << 9) / i_796_;
				} else {
					anInt988 = -1;
					anInt989 = -1;
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("97939, " + i + ", " + i_794_ + ", " + i_795_ + ", " + i_796_ + ", "
					+ runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void processClickingPrivateMessages() {
		try {
			if (anInt1220 != 0) {
				int lineIndex = 0;
				if (systemUpdateTime != 0) {
					lineIndex = 1;
				}
				for (int i = 0; i < 100; i++) {
					if (chatboxMessage[i] != null) {
						int messageType = chatboxMessageType[i];
						String name = chatboxMessageName[i];
						if (name != null) {
							if (name.startsWith("@cr1@") || name.startsWith("@cr2@")) {
								name = name.substring(5);
							}
						}
						if ((messageType == 3 || messageType == 7)
								&& (messageType == 7 || privateChatSetting == 0 || privateChatSetting == 1
										&& method109(false, name))) {
							int areaHeight = 329 - (lineIndex * 13);
							if (mouseEventX > 4 && mouseEventY - 4 > areaHeight - 10
									&& mouseEventY - 4 <= areaHeight + 3) {
								int areaWidth = fontNormal.getStringEffectWidth("From:  " + name + chatboxMessage[i]) + 25;
								if (areaWidth > 450) {
									areaWidth = 450;
								}
								if (mouseEventX < 4 + areaWidth) {
									if (playerRights >= 1) {
										menuActionNames[menuActionRow] = "Report abuse @whi@" + name;
										menuActionIndexes[menuActionRow] = 2606;
										menuActionRow++;
									}
									menuActionNames[menuActionRow] = "Add ignore @whi@" + name;
									menuActionIndexes[menuActionRow] = 2042;
									menuActionRow++;
									menuActionNames[menuActionRow] = "Add friend @whi@" + name;
									menuActionIndexes[menuActionRow] = 2337;
									menuActionRow++;
								}
							}
							if (++lineIndex >= 5) {
								break;
							}
						}
						if ((messageType == 5 || messageType == 6) && privateChatSetting < 2 && ++lineIndex >= 5) {
							break;
						}
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("61314, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	private final void method130(int i, int i_809_, int i_810_, int i_811_, int i_812_, int i_813_, int i_814_,
			int i_815_, int i_816_, int i_817_) {
		try {
			SpawnObjectNode spawnobjectnode = null;
			for (SpawnObjectNode spawnobjectnode_818_ = (SpawnObjectNode) aLinkedList1204.getBack(); spawnobjectnode_818_ != null; spawnobjectnode_818_ = (SpawnObjectNode) aLinkedList1204
					.getPrevious()) {
				if (spawnobjectnode_818_.plane == i_815_ && spawnobjectnode_818_.x == i_816_
						&& spawnobjectnode_818_.y == i_813_ && spawnobjectnode_818_.type == i_812_) {
					spawnobjectnode = spawnobjectnode_818_;
					break;
				}
			}
			if (spawnobjectnode == null) {
				spawnobjectnode = new SpawnObjectNode();
				spawnobjectnode.plane = i_815_;
				spawnobjectnode.type = i_812_;
				spawnobjectnode.x = i_816_;
				spawnobjectnode.y = i_813_;
				method89(spawnobjectnode);
				aLinkedList1204.insertBack(spawnobjectnode);
			}
			spawnobjectnode.anInt1341 = i_810_;
			spawnobjectnode.anInt1343 = i_814_;
			spawnobjectnode.anInt1342 = i_811_;
			spawnobjectnode.anInt1352 = i_817_;
			spawnobjectnode.anInt1344 = i_809_;
			if (i <= 0) {
				return;
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("83646, " + i + ", " + i_809_ + ", " + i_810_ + ", " + i_811_ + ", " + i_812_ + ", "
					+ i_813_ + ", " + i_814_ + ", " + i_815_ + ", " + i_816_ + ", " + i_817_ + ", "
					+ runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final boolean method131(Widget widget) {
		try {
			if (widget.conditionTypes == null) {
				return false;
			}
			for (int i = 0; i < widget.conditionTypes.length; i++) {
				int opcode = parseWidgetOpcode(widget, i);
				int condition = widget.conditionValues[i];
				if (widget.conditionTypes[i] == 2) {
					if (opcode >= condition) {
						return false;
					}
				} else if (widget.conditionTypes[i] == 3) {
					if (opcode <= condition) {
						return false;
					}
				} else if (widget.conditionTypes[i] == 4) {
					if (opcode == condition) {
						return false;
					}
				} else if (opcode != condition) {
					return false;
				}
			}
			return true;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("43472, " + widget + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final DataInputStream jaggrabRequest(String file) throws IOException {
		if (!aBoolean897) {
			if (SignLink.applet != null) {
				return SignLink.getURLStream(file);
			}
			return new DataInputStream(new URL(getCodeBase(), file).openStream());
		}
		if (jaggrabSocket != null) {
			try {
				jaggrabSocket.close();
			} catch (Exception exception) {
				/* empty */
			}
			jaggrabSocket = null;
		}
		jaggrabSocket = createSocket(43595);
		jaggrabSocket.setSoTimeout(10000);
		InputStream in = jaggrabSocket.getInputStream();
		OutputStream out = jaggrabSocket.getOutputStream();
		out.write(("JAGGRAB /" + file + "\n\n").getBytes());
		return new DataInputStream(in);
	}

	public final void method133() {
		do {
			try {
				int i = 256;
				if (anInt1065 > 0) {
					for (int i_821_ = 0; i_821_ < 256; i_821_++) {
						if (anInt1065 > 768) {
							anIntArray875[i_821_] = method83(anIntArray876[i_821_], anIntArray877[i_821_],
									1024 - anInt1065);
						} else if (anInt1065 > 256) {
							anIntArray875[i_821_] = anIntArray877[i_821_];
						} else {
							anIntArray875[i_821_] = method83(anIntArray877[i_821_], anIntArray876[i_821_],
									256 - anInt1065);
						}
					}
				} else if (anInt1066 > 0) {
					for (int i_822_ = 0; i_822_ < 256; i_822_++) {
						if (anInt1066 > 768) {
							anIntArray875[i_822_] = method83(anIntArray876[i_822_], anIntArray878[i_822_],
									1024 - anInt1066);
						} else if (anInt1066 > 256) {
							anIntArray875[i_822_] = anIntArray878[i_822_];
						} else {
							anIntArray875[i_822_] = method83(anIntArray878[i_822_], anIntArray876[i_822_],
									256 - anInt1066);
						}
					}
				} else {
					for (int i_823_ = 0; i_823_ < 256; i_823_++) {
						anIntArray875[i_823_] = anIntArray876[i_823_];
					}
				}
				for (int i_824_ = 0; i_824_ < 33920; i_824_++) {
					flameLeftBackground.pixels[i_824_] = anImageRGB1226.pixels[i_824_];
				}
				int i_825_ = 0;
				int i_826_ = 1152;
				for (int i_827_ = 1; i_827_ < i - 1; i_827_++) {
					int i_828_ = anIntArray994[i_827_] * (i - i_827_) / i;
					int i_829_ = 22 + i_828_;
					if (i_829_ < 0) {
						i_829_ = 0;
					}
					i_825_ += i_829_;
					for (int i_830_ = i_829_; i_830_ < 128; i_830_++) {
						int i_831_ = anIntArray853[i_825_++];
						if (i_831_ != 0) {
							int i_832_ = i_831_;
							int i_833_ = 256 - i_831_;
							i_831_ = anIntArray875[i_831_];
							int i_834_ = flameLeftBackground.pixels[i_826_];
							flameLeftBackground.pixels[i_826_++] = ((i_831_ & 0xff00ff) * i_832_ + (i_834_ & 0xff00ff)
									* i_833_ & ~0xff00ff)
									+ ((i_831_ & 0xff00) * i_832_ + (i_834_ & 0xff00) * i_833_ & 0xff0000) >> 8;
						} else {
							i_826_++;
						}
					}
					i_826_ += i_829_;
				}
				flameLeftBackground.drawGraphics(0, 0, gameGraphics);
				for (int i_835_ = 0; i_835_ < 33920; i_835_++) {
					flameRightBackground.pixels[i_835_] = anImageRGB1227.pixels[i_835_];
				}
				i_825_ = 0;
				i_826_ = 1176;
				for (int i_836_ = 1; i_836_ < i - 1; i_836_++) {
					int i_837_ = anIntArray994[i_836_] * (i - i_836_) / i;
					int i_838_ = 103 - i_837_;
					i_826_ += i_837_;
					for (int i_839_ = 0; i_839_ < i_838_; i_839_++) {
						int i_840_ = anIntArray853[i_825_++];
						if (i_840_ != 0) {
							int i_841_ = i_840_;
							int i_842_ = 256 - i_840_;
							i_840_ = anIntArray875[i_840_];
							int i_843_ = flameRightBackground.pixels[i_826_];
							flameRightBackground.pixels[i_826_++] = ((i_840_ & 0xff00ff) * i_841_ + (i_843_ & 0xff00ff)
									* i_842_ & ~0xff00ff)
									+ ((i_840_ & 0xff00) * i_841_ + (i_843_ & 0xff00) * i_842_ & 0xff0000) >> 8;
						} else {
							i_826_++;
						}
					}
					i_825_ += 128 - i_838_;
					i_826_ += 128 - i_838_ - i_837_;
				}
				flameRightBackground.drawGraphics(637, 0, gameGraphics);
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("45513, " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	private final void method134(Buffer buffer) {
		do {
			int i_844_ = buffer.getBits(8);
			if (i_844_ < playerCount) {
				for (int i_845_ = i_844_; i_845_ < playerCount; i_845_++) {
					anIntArray865[anInt864++] = anIntArray917[i_845_];
				}
			}
			if (i_844_ > playerCount) {
				SignLink.reportError(aString1198 + " Too many players");
				throw new RuntimeException("eek");
			}
			playerCount = 0;
			for (int i_846_ = 0; i_846_ < i_844_; i_846_++) {
				int i_847_ = anIntArray917[i_846_];
				Player player = players[i_847_];
				int i_848_ = buffer.getBits(1);
				if (i_848_ == 0) {
					anIntArray917[playerCount++] = i_847_;
					player.anInt1557 = Client.currentCycle;
				} else {
					int i_849_ = buffer.getBits(2);
					if (i_849_ == 0) {
						anIntArray917[playerCount++] = i_847_;
						player.anInt1557 = Client.currentCycle;
						anIntArray919[anInt918++] = i_847_;
					} else if (i_849_ == 1) {
						anIntArray917[playerCount++] = i_847_;
						player.anInt1557 = Client.currentCycle;
						int i_850_ = buffer.getBits(3);
						player.move(i_850_, false);
						int i_851_ = buffer.getBits(1);
						if (i_851_ == 1) {
							anIntArray919[anInt918++] = i_847_;
						}
					} else if (i_849_ == 2) {
						anIntArray917[playerCount++] = i_847_;
						player.anInt1557 = Client.currentCycle;
						int i_852_ = buffer.getBits(3);
						player.move(i_852_, true);
						int i_853_ = buffer.getBits(3);
						player.move(i_853_, true);
						int i_854_ = buffer.getBits(1);
						if (i_854_ == 1) {
							anIntArray919[anInt918++] = i_847_;
						}
					} else if (i_849_ == 3) {
						anIntArray865[anInt864++] = i_847_;
					}
				}
			}
			break;
		} while (false);
	}

	public final void renderLoginScreen(boolean hideButtons) {
		do {
			try {
				method64();
				aProducingGraphicsBuffer1134.createRasterizer();
				titleboxImage.drawImage(0, 0);
				int i = 360;
				int i_856_ = 200;
				if (loginScreenState == 0) {
					int i_857_ = i_856_ / 2 + 80;
					fontSmall.drawString(onDemandRequester.message, i / 2, i_857_, 0x75A9A9, true);
					i_857_ = i_856_ / 2 - 20;
					fontBold.drawString("Welcome to RuneScape", i / 2, i_857_, 0xFFFF00, true);
					i_857_ += 30;
					int i_858_ = i / 2 - 80;
					int i_859_ = i_856_ / 2 + 20;
					titleboxButtonImage.drawImage(i_858_ - 73, i_859_ - 20);
					fontBold.drawString("New User", i_858_, i_859_ + 5, 0xFFFFFF, true);
					i_858_ = i / 2 + 80;
					titleboxButtonImage.drawImage(i_858_ - 73, i_859_ - 20);
					fontBold.drawString("Existing User", i_858_, i_859_ + 5, 0xFFFFFF, true);
				}
				if (loginScreenState == 2) {
					int i_860_ = i_856_ / 2 - 40;
					if (loginMessage1.length() > 0) {
						fontBold.drawString(loginMessage1, i / 2, i_860_ - 15, 0xFFFF00, true);
						fontBold.drawString(loginMessage2, i / 2, i_860_, 0xFFFF00, true);
						i_860_ += 30;
					} else {
						fontBold.drawString(loginMessage2, i / 2, i_860_ - 7, 0xFFFF00, true);
						i_860_ += 30;
					}
					fontBold.drawShadowedString("Username: " + aString1198
							+ (anInt1241 == 0 & Client.currentCycle % 40 < 20 ? "@yel@|" : ""), i / 2 - 90, i_860_,
							true, 0xFFFFFF);
					i_860_ += 15;
					fontBold.drawShadowedString("Password: " + TextUtils.censorPassword(aString1199)
							+ (anInt1241 == 1 & Client.currentCycle % 40 < 20 ? "@yel@|" : ""), i / 2 - 88, i_860_,
							true, 0xFFFFFF);
					i_860_ += 15;
					if (!hideButtons) {
						int i_861_ = i / 2 - 80;
						int i_862_ = i_856_ / 2 + 50;
						titleboxButtonImage.drawImage(i_861_ - 73, i_862_ - 20);
						fontBold.drawString("Login", i_861_, i_862_ + 5, 0xFFFFFF, true);
						i_861_ = i / 2 + 80;
						titleboxButtonImage.drawImage(i_861_ - 73, i_862_ - 20);
						fontBold.drawString("Cancel", i_861_, i_862_ + 5, 0xFFFFFF, true);
					}
				}
				if (loginScreenState == 3) {
					fontBold.drawString("Create a free account", i / 2, i_856_ / 2 - 60, 0xFFFF00, true);
					int i_863_ = i_856_ / 2 - 35;
					fontBold.drawString("To create a new account you need to", i / 2, i_863_, 0xFFFFFF, true);
					i_863_ += 15;
					fontBold.drawString("go back to the main RuneScape webpage", i / 2, i_863_, 0xFFFFFF, true);
					i_863_ += 15;
					fontBold.drawString("and choose the red 'create account'", i / 2, i_863_, 0xFFFFFF, true);
					i_863_ += 15;
					fontBold.drawString("button at the top right of that page.", i / 2, i_863_, 0xFFFFFF, true);
					i_863_ += 15;
					int i_864_ = i / 2;
					int i_865_ = i_856_ / 2 + 50;
					titleboxButtonImage.drawImage(i_864_ - 73, i_865_ - 20);
					fontBold.drawString("Cancel", i_864_, i_865_ + 5, 0xFFFFFF, true);
				}
				aProducingGraphicsBuffer1134.drawGraphics(202, 171, gameGraphics);
				if (!drawGameframe) {
					break;
				}
				drawGameframe = false;
				aProducingGraphicsBuffer1132.drawGraphics(128, 0, gameGraphics);
				aProducingGraphicsBuffer1133.drawGraphics(202, 371, gameGraphics);
				aProducingGraphicsBuffer1137.drawGraphics(0, 265, gameGraphics);
				aProducingGraphicsBuffer1138.drawGraphics(562, 265, gameGraphics);
				aProducingGraphicsBuffer1139.drawGraphics(128, 171, gameGraphics);
				aProducingGraphicsBuffer1140.drawGraphics(562, 171, gameGraphics);
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("92290, " + hideButtons + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void method136(byte b) {
		try {
			aBoolean987 = true;
			if (b != 59) {
			}
			try {
				long l = System.currentTimeMillis();
				int i = 0;
				int i_866_ = 20;
				while (aBoolean856) {
					anInt1233++;
					renderFlames();
					renderFlames();
					method133();
					if (++i > 10) {
						long l_867_ = System.currentTimeMillis();
						int i_868_ = (int) (l_867_ - l) / 10 - i_866_;
						i_866_ = 40 - i_868_;
						if (i_866_ < 5) {
							i_866_ = 5;
						}
						i = 0;
						l = l_867_;
					}
					try {
						Thread.sleep(i_866_);
					} catch (Exception exception) {
						/* empty */
					}
				}
			} catch (Exception exception) {
				/* empty */
			}
			aBoolean987 = false;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("48378, " + b + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method137(int i, Buffer buffer, int i_869_) {
		try {
			while (i >= 0) {
				i_869_ = -1;
			}
			if (i_869_ == 84) {
				int i_870_ = buffer.getUnsignedByte();
				int i_871_ = playerPositionX + (i_870_ >> 4 & 0x7);
				int i_872_ = playerPositionY + (i_870_ & 0x7);
				int i_873_ = buffer.getUnsignedLEShort();
				int i_874_ = buffer.getUnsignedLEShort();
				int i_875_ = buffer.getUnsignedLEShort();
				if (i_871_ >= 0 && i_872_ >= 0 && i_871_ < 104 && i_872_ < 104) {
					LinkedList linkedlist = groundItemNodes[currentSceneIndex][i_871_][i_872_];
					if (linkedlist != null) {
						for (Item item = (Item) linkedlist.getBack(); item != null; item = (Item) linkedlist
								.getPrevious()) {
							if (item.itemId == (i_873_ & 0x7fff) && item.itemCount == i_874_) {
								item.itemCount = i_875_;
								break;
							}
						}
						method25(i_871_, i_872_);
					}
				}
			} else {
				if (i_869_ == 105) {
					int i_876_ = buffer.getUnsignedByte();
					int i_877_ = playerPositionX + (i_876_ >> 4 & 0x7);
					int i_878_ = playerPositionY + (i_876_ & 0x7);
					int i_879_ = buffer.getUnsignedLEShort();
					int i_880_ = buffer.getUnsignedByte();
					int i_881_ = i_880_ >> 4 & 0xf;
					int i_882_ = i_880_ & 0x7;
					if (Client.localPlayer.pathX[0] >= i_877_ - i_881_
							&& Client.localPlayer.pathX[0] <= i_877_ + i_881_
							&& Client.localPlayer.pathY[0] >= i_878_ - i_881_
							&& Client.localPlayer.pathY[0] <= i_878_ + i_881_ && aBoolean873 && !Client.lowMemory
							&& trackCount < 50) {
						trackIndexes[trackCount] = i_879_;
						trackLoop[trackCount] = i_882_;
						trackDelay[trackCount] = SoundTrack.trackDelays[i_879_];
						trackCount++;
					}
				}
				if (i_869_ == 215) {
					int i_883_ = buffer.getUnsignedLEShortA();
					int i_884_ = buffer.getUnsignedByteS();
					int i_885_ = playerPositionX + (i_884_ >> 4 & 0x7);
					int i_886_ = playerPositionY + (i_884_ & 0x7);
					int i_887_ = buffer.getUnsignedLEShortA();
					int i_888_ = buffer.getUnsignedLEShort();
					if (i_885_ >= 0 && i_886_ >= 0 && i_885_ < 104 && i_886_ < 104 && i_887_ != anInt909) {
						Item item = new Item();
						item.itemId = i_883_;
						item.itemCount = i_888_;
						if (groundItemNodes[currentSceneIndex][i_885_][i_886_] == null) {
							groundItemNodes[currentSceneIndex][i_885_][i_886_] = new LinkedList();
						}
						groundItemNodes[currentSceneIndex][i_885_][i_886_].insertBack(item);
						method25(i_885_, i_886_);
					}
				} else if (i_869_ == 156) {
					int i_889_ = buffer.getUnsignedByteA();
					int i_890_ = playerPositionX + (i_889_ >> 4 & 0x7);
					int i_891_ = playerPositionY + (i_889_ & 0x7);
					int i_892_ = buffer.getUnsignedLEShort();
					if (i_890_ >= 0 && i_891_ >= 0 && i_890_ < 104 && i_891_ < 104) {
						LinkedList linkedlist = groundItemNodes[currentSceneIndex][i_890_][i_891_];
						if (linkedlist != null) {
							for (Item item = (Item) linkedlist.getBack(); item != null; item = (Item) linkedlist
									.getPrevious()) {
								if (item.itemId == (i_892_ & 0x7fff)) {
									item.remove();
									break;
								}
							}
							if (linkedlist.getBack() == null) {
								groundItemNodes[currentSceneIndex][i_890_][i_891_] = null;
							}
							method25(i_890_, i_891_);
						}
					}
				} else if (i_869_ == 160) {
					int i_893_ = buffer.getUnsignedByteS();
					int i_894_ = playerPositionX + (i_893_ >> 4 & 0x7);
					int i_895_ = playerPositionY + (i_893_ & 0x7);
					int i_896_ = buffer.getUnsignedByteS();
					int i_897_ = i_896_ >> 2;
					int i_898_ = i_896_ & 0x3;
					int i_899_ = anIntArray1202[i_897_];
					int i_900_ = buffer.getUnsignedLEShortA();
					if (i_894_ >= 0 && i_895_ >= 0 && i_894_ < 103 && i_895_ < 103) {
						int i_901_ = anIntArrayArrayArray1239[currentSceneIndex][i_894_][i_895_];
						int i_902_ = anIntArrayArrayArray1239[currentSceneIndex][i_894_ + 1][i_895_];
						int i_903_ = anIntArrayArrayArray1239[currentSceneIndex][i_894_ + 1][i_895_ + 1];
						int i_904_ = anIntArrayArrayArray1239[currentSceneIndex][i_894_][i_895_ + 1];
						if (i_899_ == 0) {
							Wall wall = currentScene.method518(currentSceneIndex, i_894_, i_895_, false);
							if (wall != null) {
								int i_905_ = wall.anInt771 >> 14 & 0x7fff;
								if (i_897_ == 2) {
									wall.aRenderable769 = new GameObject(i_905_, 4 + i_898_, 2, i_902_, (byte) 7,
											i_903_, i_901_, i_904_, i_900_, false);
									wall.aRenderable770 = new GameObject(i_905_, i_898_ + 1 & 0x3, 2, i_902_, (byte) 7,
											i_903_, i_901_, i_904_, i_900_, false);
								} else {
									wall.aRenderable769 = new GameObject(i_905_, i_898_, i_897_, i_902_, (byte) 7,
											i_903_, i_901_, i_904_, i_900_, false);
								}
							}
						}
						if (i_899_ == 1) {
							WallDecoration walldecoration = currentScene.method519(i_894_, 866, i_895_,
									currentSceneIndex);
							if (walldecoration != null) {
								walldecoration.renderable = new GameObject(walldecoration.hash >> 14 & 0x7fff, 0, 4,
										i_902_, (byte) 7, i_903_, i_901_, i_904_, i_900_, false);
							}
						}
						if (i_899_ == 2) {
							SceneSpawnRequest scenespawnrequest = currentScene.method520(i_894_, i_895_, (byte) 4,
									currentSceneIndex);
							if (i_897_ == 11) {
								i_897_ = 10;
							}
							if (scenespawnrequest != null) {
								scenespawnrequest.aRenderable601 = new GameObject(
										scenespawnrequest.anInt609 >> 14 & 0x7fff, i_898_, i_897_, i_902_, (byte) 7,
										i_903_, i_901_, i_904_, i_900_, false);
							}
						}
						if (i_899_ == 3) {
							FloorDecoration floordecoration = currentScene.method521(i_895_, i_894_, currentSceneIndex,
									0);
							if (floordecoration != null) {
								floordecoration.renderable = new GameObject(floordecoration.hash >> 14 & 0x7fff,
										i_898_, 22, i_902_, (byte) 7, i_903_, i_901_, i_904_, i_900_, false);
							}
						}
					}
				} else {
					if (i_869_ == 147) {
						int i_906_ = buffer.getUnsignedByteS();
						int i_907_ = playerPositionX + (i_906_ >> 4 & 0x7);
						int i_908_ = playerPositionY + (i_906_ & 0x7);
						int i_909_ = buffer.getUnsignedLEShort();
						int i_910_ = buffer.getByteS();
						int i_911_ = buffer.getUnsignedShort();
						int i_912_ = buffer.getByteC();
						int i_913_ = buffer.getUnsignedLEShort();
						int i_914_ = buffer.getUnsignedByteS();
						int i_915_ = i_914_ >> 2;
						int i_916_ = i_914_ & 0x3;
						int i_917_ = anIntArray1202[i_915_];
						int i_918_ = buffer.get();
						int i_919_ = buffer.getUnsignedLEShort();
						int i_920_ = buffer.getByteC();
						Player player;
						if (i_909_ == anInt909) {
							player = Client.localPlayer;
						} else {
							player = players[i_909_];
						}
						if (player != null) {
							GameObjectDefinition gameobjectdefinition = GameObjectDefinition.getDefinition(i_919_);
							int i_921_ = anIntArrayArrayArray1239[currentSceneIndex][i_907_][i_908_];
							int i_922_ = anIntArrayArrayArray1239[currentSceneIndex][i_907_ + 1][i_908_];
							int i_923_ = anIntArrayArrayArray1239[currentSceneIndex][i_907_ + 1][i_908_ + 1];
							int i_924_ = anIntArrayArrayArray1239[currentSceneIndex][i_907_][i_908_ + 1];
							Model model = gameobjectdefinition.getGameObjectModel(i_915_, i_916_, i_921_, i_922_,
									i_923_, i_924_, -1);
							if (model != null) {
								method130(404, i_913_ + 1, -1, 0, i_917_, i_908_, 0, currentSceneIndex, i_907_,
										i_911_ + 1);
								player.anInt1727 = i_911_ + Client.currentCycle;
								player.anInt1728 = i_913_ + Client.currentCycle;
								player.aModel1734 = model;
								int i_925_ = gameobjectdefinition.width;
								int i_926_ = gameobjectdefinition.height;
								if (i_916_ == 1 || i_916_ == 3) {
									i_925_ = gameobjectdefinition.height;
									i_926_ = gameobjectdefinition.width;
								}
								player.anInt1731 = i_907_ * 128 + i_925_ * 64;
								player.anInt1733 = i_908_ * 128 + i_926_ * 64;
								player.anInt1732 = method42(currentSceneIndex, player.anInt1733, true, player.anInt1731);
								if (i_918_ > i_910_) {
									int i_927_ = i_918_;
									i_918_ = i_910_;
									i_910_ = i_927_;
								}
								if (i_920_ > i_912_) {
									int i_928_ = i_920_;
									i_920_ = i_912_;
									i_912_ = i_928_;
								}
								player.anInt1739 = i_907_ + i_918_;
								player.anInt1741 = i_907_ + i_910_;
								player.anInt1740 = i_908_ + i_920_;
								player.anInt1742 = i_908_ + i_912_;
							}
						}
					}
					if (i_869_ == 151) {
						int i_929_ = buffer.getUnsignedByteA();
						int i_930_ = playerPositionX + (i_929_ >> 4 & 0x7);
						int i_931_ = playerPositionY + (i_929_ & 0x7);
						int i_932_ = buffer.getUnsignedShort();
						int i_933_ = buffer.getUnsignedByteS();
						int i_934_ = i_933_ >> 2;
						int i_935_ = i_933_ & 0x3;
						int i_936_ = anIntArray1202[i_934_];
						if (i_930_ >= 0 && i_931_ >= 0 && i_930_ < 104 && i_931_ < 104) {
							method130(404, -1, i_932_, i_935_, i_936_, i_931_, i_934_, currentSceneIndex, i_930_, 0);
						}
					} else if (i_869_ == 4) {
						int i_937_ = buffer.getUnsignedByte();
						int i_938_ = playerPositionX + (i_937_ >> 4 & 0x7);
						int i_939_ = playerPositionY + (i_937_ & 0x7);
						int i_940_ = buffer.getUnsignedLEShort();
						int i_941_ = buffer.getUnsignedByte();
						int i_942_ = buffer.getUnsignedLEShort();
						if (i_938_ >= 0 && i_939_ >= 0 && i_938_ < 104 && i_939_ < 104) {
							i_938_ = i_938_ * 128 + 64;
							i_939_ = i_939_ * 128 + 64;
							AnimableObject animableobject = new AnimableObject(currentSceneIndex, Client.currentCycle,
									i_942_, i_940_, method42(currentSceneIndex, i_939_, true, i_938_) - i_941_, i_939_,
									i_938_);
							aLinkedList1081.insertBack(animableobject);
						}
					} else if (i_869_ == 44) {
						int i_943_ = buffer.getUnsignedShortA();
						int i_944_ = buffer.getUnsignedLEShort();
						int i_945_ = buffer.getUnsignedByte();
						int i_946_ = playerPositionX + (i_945_ >> 4 & 0x7);
						int i_947_ = playerPositionY + (i_945_ & 0x7);
						if (i_946_ >= 0 && i_947_ >= 0 && i_946_ < 104 && i_947_ < 104) {
							Item item = new Item();
							item.itemId = i_943_;
							item.itemCount = i_944_;
							if (groundItemNodes[currentSceneIndex][i_946_][i_947_] == null) {
								groundItemNodes[currentSceneIndex][i_946_][i_947_] = new LinkedList();
							}
							groundItemNodes[currentSceneIndex][i_946_][i_947_].insertBack(item);
							method25(i_946_, i_947_);
						}
					} else if (i_869_ == 101) {
						int i_948_ = buffer.getUnsignedByteC();
						int i_949_ = i_948_ >> 2;
						int i_950_ = i_948_ & 0x3;
						int i_951_ = anIntArray1202[i_949_];
						int i_952_ = buffer.getUnsignedByte();
						int i_953_ = playerPositionX + (i_952_ >> 4 & 0x7);
						int i_954_ = playerPositionY + (i_952_ & 0x7);
						if (i_953_ >= 0 && i_954_ >= 0 && i_953_ < 104 && i_954_ < 104) {
							method130(404, -1, -1, i_950_, i_951_, i_954_, i_949_, currentSceneIndex, i_953_, 0);
						}
					} else if (i_869_ == 117) {
						int projectileAngle = buffer.getUnsignedByte();
						int projectileX = playerPositionX + (projectileAngle >> 4 & 0x7);
						int projectileY = playerPositionY + (projectileAngle & 0x7);
						int projectileOffsetX = projectileX + buffer.get();
						int projectileOffsetY = projectileY + buffer.get();
						int projectileAttacked = buffer.getShort();
						int projectileEffectId = buffer.getUnsignedLEShort();
						int projectileStartHeight = buffer.getUnsignedByte() * 4;
						int projectileEndHight = buffer.getUnsignedByte() * 4;
						int projectileCreatedTime = buffer.getUnsignedLEShort();
						int projectileSpeed = buffer.getUnsignedLEShort();
						int projectileInitialSlope = buffer.getUnsignedByte();
						int projectileInitialDistanceFromSource = buffer.getUnsignedByte();
						if (projectileX >= 0 && projectileY >= 0 && projectileX < 104 && projectileY < 104
								&& projectileOffsetX >= 0 && projectileOffsetY >= 0 && projectileOffsetX < 104
								&& projectileOffsetY < 104 && projectileEffectId != 65535) {
							projectileX = projectileX * 128 + 64;
							projectileY = projectileY * 128 + 64;
							projectileOffsetX = projectileOffsetX * 128 + 64;
							projectileOffsetY = projectileOffsetY * 128 + 64;
							Projectile projectile = new Projectile(projectileInitialSlope, projectileEndHight,
									projectileCreatedTime + Client.currentCycle, projectileSpeed + Client.currentCycle,
									projectileInitialDistanceFromSource, currentSceneIndex, method42(currentSceneIndex,
											projectileY, true, projectileX) - projectileStartHeight, projectileY,
									projectileX, projectileAttacked, projectileEffectId);
							projectile.trackTarget(projectileCreatedTime + Client.currentCycle, projectileOffsetY,
									method42(currentSceneIndex, projectileOffsetY, true, projectileOffsetX)
											- projectileEndHight, projectileOffsetX);
							projectileList.insertBack(projectile);
						}
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("29026, " + i + ", " + buffer + ", " + i_869_ + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public static final void setLowMemory() {
		try {
			Scene.lowMemory = true;
			Rasterizer3D.lowMemory = true;
			Client.lowMemory = true;
			Region.lowMemory = true;
			GameObjectDefinition.lowMemory = true;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("23911, " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	private final void method139(Buffer buffer, int i, int i_968_) {
		try {
			if (i >= 0) {
			}
			buffer.initBitAccess();
			int i_969_ = buffer.getBits(8);
			if (i_969_ < actorCount) {
				for (int i_970_ = i_969_; i_970_ < actorCount; i_970_++) {
					anIntArray865[anInt864++] = anIntArray862[i_970_];
				}
			}
			if (i_969_ > actorCount) {
				SignLink.reportError(aString1198 + " Too many npcs");
				throw new RuntimeException("eek");
			}
			actorCount = 0;
			for (int i_971_ = 0; i_971_ < i_969_; i_971_++) {
				int i_972_ = anIntArray862[i_971_];
				Npc npc = actors[i_972_];
				int i_973_ = buffer.getBits(1);
				if (i_973_ == 0) {
					anIntArray862[actorCount++] = i_972_;
					npc.anInt1557 = Client.currentCycle;
				} else {
					int i_974_ = buffer.getBits(2);
					if (i_974_ == 0) {
						anIntArray862[actorCount++] = i_972_;
						npc.anInt1557 = Client.currentCycle;
						anIntArray919[anInt918++] = i_972_;
					} else if (i_974_ == 1) {
						anIntArray862[actorCount++] = i_972_;
						npc.anInt1557 = Client.currentCycle;
						int i_975_ = buffer.getBits(3);
						npc.move(i_975_, false);
						int i_976_ = buffer.getBits(1);
						if (i_976_ == 1) {
							anIntArray919[anInt918++] = i_972_;
						}
					} else if (i_974_ == 2) {
						anIntArray862[actorCount++] = i_972_;
						npc.anInt1557 = Client.currentCycle;
						int i_977_ = buffer.getBits(3);
						npc.move(i_977_, true);
						int i_978_ = buffer.getBits(3);
						npc.move(i_978_, true);
						int i_979_ = buffer.getBits(1);
						if (i_979_ == 1) {
							anIntArray919[anInt918++] = i_972_;
						}
					} else if (i_974_ == 3) {
						anIntArray865[anInt864++] = i_972_;
					}
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("60808, " + buffer + ", " + i + ", " + i_968_ + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void processLoginScreen() {
		do {
			try {
				if (loginScreenState == 0) {
					int i = width / 2 - 80;
					int i_980_ = height / 2 + 20;
					i_980_ += 20;
					if (clickType == 1 && clickX >= i - 75 && clickX <= i + 75 && clickY >= i_980_ - 20
							&& clickY <= i_980_ + 20) {
						loginScreenState = 3;
						anInt1241 = 0;
					}
					i = width / 2 + 80;
					if (clickType == 1 && clickX >= i - 75 && clickX <= i + 75 && clickY >= i_980_ - 20
							&& clickY <= i_980_ + 20) {
						loginMessage1 = "";
						loginMessage2 = "Enter your username & password.";
						loginScreenState = 2;
						anInt1241 = 0;
					}
				} else if (loginScreenState == 2) {
					int i = height / 2 - 40;
					i += 30;
					i += 25;
					if (clickType == 1 && clickY >= i - 15 && clickY < i) {
						anInt1241 = 0;
					}
					i += 15;
					if (clickType == 1 && clickY >= i - 15 && clickY < i) {
						anInt1241 = 1;
					}
					i += 15;
					int i_981_ = width / 2 - 80;
					int i_982_ = height / 2 + 50;
					i_982_ += 20;
					if (clickType == 1 && clickX >= i_981_ - 75 && clickX <= i_981_ + 75 && clickY >= i_982_ - 20
							&& clickY <= i_982_ + 20) {
						anInt1063 = 0;
						method84(aString1198, aString1199, false);
						if (loggedIn) {
							break;
						}
					}
					i_981_ = width / 2 + 80;
					if (clickType == 1 && clickX >= i_981_ - 75 && clickX <= i_981_ + 75 && clickY >= i_982_ - 20
							&& clickY <= i_982_ + 20) {
						loginScreenState = 0;
						aString1198 = "";
						aString1199 = "";
					}
					for (;;) {
						int character = this.readCharacter();
						if (character == -1) {
							break;
						}
						boolean validCharacter = false;
						for (int i_ = 0; i_ < Client.VALID_CHARACTERS.length(); i_++) {
							if (character == Client.VALID_CHARACTERS.charAt(i_)) {
								validCharacter = true;
								break;
							}
						}
						if (anInt1241 == 0) {
							if (character == 8 && aString1198.length() > 0) {
								aString1198 = aString1198.substring(0, aString1198.length() - 1);
							}
							if (character == 9 || character == 10 || character == 13) {
								anInt1241 = 1;
							}
							if (validCharacter) {
								aString1198 += (char) character;
							}
							if (aString1198.length() > 12) {
								aString1198 = aString1198.substring(0, 12);
							}
						} else if (anInt1241 == 1) {
							if (character == 8 && aString1199.length() > 0) {
								aString1199 = aString1199.substring(0, aString1199.length() - 1);
							}
							if (character == 9 || character == 10 || character == 13) {
								anInt1241 = 0;
							}
							if (validCharacter) {
								aString1199 += (char) character;
							}
							if (aString1199.length() > 20) {
								aString1199 = aString1199.substring(0, 20);
							}
						}
					}
				} else {
					if (loginScreenState != 3) {
						break;
					}
					int i = width / 2;
					int i_986_ = height / 2 + 50;
					i_986_ += 20;
					if (clickType != 1 || clickX < i - 75 || clickX > i + 75 || clickY < i_986_ - 20
							|| clickY > i_986_ + 20) {
						break;
					}
					loginScreenState = 0;
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("8370, " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	public final void method141(ImageRGB imagergb, int i, int i_987_) {
		try {
			int i_988_ = anInt1210 + anInt1234 & 0x7ff;
			int i_989_ = i * i + i_987_ * i_987_;
			if (i_989_ <= 6400) {
				int i_990_ = Model.anIntArray1682[i_988_];
				int i_991_ = Model.anIntArray1683[i_988_];
				i_990_ = i_990_ * 256 / (anInt1195 + 256);
				i_991_ = i_991_ * 256 / (anInt1195 + 256);
				int i_992_ = i_987_ * i_990_ + i * i_991_ >> 16;
				int i_993_ = i_987_ * i_991_ - i * i_990_ >> 16;
				if (i_989_ > 2500) {
					imagergb.method351(minimapBackgroundImage, false, 83 - i_993_ - imagergb.maxHeight / 2 - 4, 94
							+ i_992_ - imagergb.maxWidth / 2 + 4);
				} else {
					imagergb.drawImage(94 + i_992_ - imagergb.maxWidth / 2 + 4, 83 - i_993_ - imagergb.maxHeight / 2
							- 4);
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("45113, " + imagergb + ", " + i + ", " + i_987_ + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	private final void method142(int y, int i_994_, int i_995_, int i_996_, int x, int i_998_, int i_999_, int i_1000_) {
		do {
			try {
				if (i_1000_ < 4 || i_1000_ > 4) {
					opcode = inBuffer.getUnsignedByte();
				}
				if (x < 1 || y < 1 || x > 102 || y > 102) {
					break;
				}
				if (!Client.lowMemory || i_994_ == currentSceneIndex) {
					int i_1001_ = 0;
					int i_1002_ = -1;
					if (i_998_ == 0) {
						i_1001_ = currentScene.method522(i_994_, x, y);
					}
					if (i_998_ == 1) {
						i_1001_ = currentScene.method523(i_994_, x, 0, y);
					}
					if (i_998_ == 2) {
						i_1001_ = currentScene.method524(i_994_, x, y);
					}
					if (i_998_ == 3) {
						i_1001_ = currentScene.getFloorHash(i_994_, x, y);
					}
					if (i_1001_ != 0) {
						int i_1004_ = currentScene.method526(i_994_, x, y, i_1001_);
						i_1002_ = i_1001_ >> 14 & 0x7fff;
						int position = i_1004_ & 0x1f;
						int orientation = i_1004_ >> 6;
						if (i_998_ == 0) {
							currentScene.method513(x, i_994_, y, (byte) -119);
							GameObjectDefinition gameobjectdefinition = GameObjectDefinition.getDefinition(i_1002_);
							if (gameobjectdefinition.solid) {
								currentCollisionMap[i_994_].unmarkWall(orientation, x, y, position,
										gameobjectdefinition.walkable);
							}
						}
						if (i_998_ == 1) {
							currentScene.method514(0, y, i_994_, x);
						}
						if (i_998_ == 2) {
							currentScene.method515(i_994_, -978, x, y);
							GameObjectDefinition gameobjectdefinition = GameObjectDefinition.getDefinition(i_1002_);
							if (x + gameobjectdefinition.width > 103 || y + gameobjectdefinition.width > 103
									|| x + gameobjectdefinition.height > 103 || y + gameobjectdefinition.height > 103) {
								break;
							}
							if (gameobjectdefinition.solid) {
								currentCollisionMap[i_994_].unmarkSolidOccupant(x, y, gameobjectdefinition.width,
										gameobjectdefinition.height, orientation, gameobjectdefinition.walkable);
							}
						}
						if (i_998_ == 3) {
							currentScene.method516((byte) 9, i_994_, y, x);
							GameObjectDefinition gameobjectdefinition = GameObjectDefinition.getDefinition(i_1002_);
							if (gameobjectdefinition.solid && gameobjectdefinition.actionsBoolean) {
								currentCollisionMap[i_994_].unmarkConcealed(x, y);
							}
						}
					}
					if (i_999_ < 0) {
						break;
					}
					int i_1007_ = i_994_;
					if (i_1007_ < 3 && (currentSceneTileFlags[1][x][y] & 0x2) == 2) {
						i_1007_++;
					}
					Region.method470(currentScene, i_995_, y, i_996_, i_1007_, currentCollisionMap[i_994_],
							anIntArrayArrayArray1239, x, i_999_, i_994_, (byte) 93);
				}
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("56911, " + y + ", " + i_994_ + ", " + i_995_ + ", " + i_996_ + ", " + x + ", "
						+ i_998_ + ", " + i_999_ + ", " + i_1000_ + ", " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	private final void updatePlayers(int i, Buffer buffer) {
		try {
			anInt864 = 0;
			anInt918 = 0;
			method117(buffer);
			method134(buffer);
			method91(buffer, i);
			method49(buffer);
			for (int i_ = 0; i_ < anInt864; i_++) {
				int playerIndex = anIntArray865[i_];
				if (players[playerIndex].anInt1557 != Client.currentCycle) {
					players[playerIndex] = null;
				}
			}
			if (buffer.offset != i) {
				SignLink.reportError("Error packet size mismatch in getplayer pos:" + buffer.offset + " psize:" + i);
				throw new RuntimeException("eek");
			}
			for (int i_ = 0; i_ < playerCount; i_++) {
				if (players[anIntArray917[i_]] == null) {
					SignLink.reportError(aString1198 + " null entry in pl list - pos:" + i_ + " size:" + playerCount);
					throw new RuntimeException("eek");
				}
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("70865, " + i + ", " + buffer + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method144(int i, int i_1012_, int i_1013_, int i_1014_, int i_1015_, int i_1016_, int i_1017_) {
		try {
			int i_1018_ = 2048 - i_1013_ & 0x7ff;
			int i_1019_ = 2048 - i_1016_ & 0x7ff;
			int i_1020_ = 0;
			int i_1021_ = 0;
			int i_1022_ = i_1012_;
			if (i != 0) {
				startup();
			}
			if (i_1018_ != 0) {
				int i_1023_ = Model.anIntArray1682[i_1018_];
				int i_1024_ = Model.anIntArray1683[i_1018_];
				int i_1025_ = i_1021_ * i_1024_ - i_1022_ * i_1023_ >> 16;
				i_1022_ = i_1021_ * i_1023_ + i_1022_ * i_1024_ >> 16;
				i_1021_ = i_1025_;
			}
			if (i_1019_ != 0) {
				int i_1026_ = Model.anIntArray1682[i_1019_];
				int i_1027_ = Model.anIntArray1683[i_1019_];
				int i_1028_ = i_1022_ * i_1026_ + i_1020_ * i_1027_ >> 16;
				i_1022_ = i_1022_ * i_1027_ - i_1020_ * i_1026_ >> 16;
				i_1020_ = i_1028_;
			}
			anInt883 = i_1014_ - i_1020_;
			anInt884 = i_1015_ - i_1021_;
			anInt885 = i_1017_ - i_1022_;
			anInt886 = i_1013_;
			anInt887 = i_1016_;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("69735, " + i + ", " + i_1012_ + ", " + i_1013_ + ", " + i_1014_ + ", " + i_1015_
					+ ", " + i_1016_ + ", " + i_1017_ + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final boolean method145(boolean bool) {
		try {
			if (!bool) {
				groundItemNodes = null;
			}
			if (bufferedConnection == null) {
				return false;
			}
			try {
				int i = bufferedConnection.getAvailable();
				if (i == 0) {
					return false;
				}
				if (opcode == -1) {
					bufferedConnection.read(inBuffer.payload, 0, 1);
					opcode = inBuffer.payload[0] & 0xff;
					if (isaacCipher != null) {
						opcode = opcode - isaacCipher.nextInt() & 0xff;
					}
					packetSize = PacketConstants.PACKET_SIZES[opcode];
					i--;
				}
				if (packetSize == -1) {
					if (i > 0) {
						bufferedConnection.read(inBuffer.payload, 0, 1);
						packetSize = inBuffer.payload[0] & 0xff;
						i--;
					} else {
						return false;
					}
				}
				if (packetSize == -2) {
					if (i > 1) {
						bufferedConnection.read(inBuffer.payload, 0, 2);
						inBuffer.offset = 0;
						packetSize = inBuffer.getUnsignedLEShort();
						i -= 2;
					} else {
						return false;
					}
				}
				if (i < packetSize) {
					return false;
				}
				inBuffer.offset = 0;
				bufferedConnection.read(inBuffer.payload, 0, packetSize);
				anInt1034 = 0;
				anInt868 = anInt867;
				anInt867 = anInt866;
				anInt866 = opcode;

				/* Update players. */
				if (opcode == 81) {
					updatePlayers(packetSize, inBuffer);
					aBoolean1105 = false;
					opcode = -1;
					return true;
				}

				/* Opens welcome screen. */
				if (opcode == 176) {
					lastRecoveryChange = inBuffer.getUnsignedByteC();
					unreadMessages = inBuffer.getUnsignedLEShortA();
					membershipAdviser = inBuffer.getUnsignedByte();
					lastAddress = inBuffer.getInt1();
					lastLogin = inBuffer.getUnsignedLEShort();
					if (lastAddress != 0 && openWidgetIndex == -1) {
						SignLink.setLastIP(TextUtils.decodeAddress(lastAddress));
						closeWidgets();
						int contentType = 650;
						if (lastRecoveryChange != 201 || membershipAdviser == 1) {
							contentType = 655;
						}
						reportWidgetName = "";
						aBoolean1183 = false;
						for (int idx = 0; idx < Widget.cache.length; idx++) {
							if (Widget.cache[idx] != null && Widget.cache[idx].contentType == contentType) {
								openWidgetIndex = Widget.cache[idx].parentIndex;
								break;
							}
						}
					}
					opcode = -1;
					return true;
				}

				/* TODO: Figure out usage */
				if (opcode == 64) {
					playerPositionX = inBuffer.getUnsignedByteC();
					playerPositionY = inBuffer.getUnsignedByteS();
					for (int x = playerPositionX; x < playerPositionX + 8; x++) {
						for (int y = playerPositionY; y < playerPositionY + 8; y++) {
							if (groundItemNodes[currentSceneIndex][x][y] != null) {
								groundItemNodes[currentSceneIndex][x][y] = null;
								method25(x, y);
							}
						}
					}
					for (SpawnObjectNode node = (SpawnObjectNode) aLinkedList1204.getBack(); node != null; node = (SpawnObjectNode) aLinkedList1204
							.getPrevious()) {
						if (node.x >= playerPositionX && node.x < playerPositionX + 8 && node.y >= playerPositionY
								&& node.y < playerPositionY + 8 && node.plane == currentSceneIndex) {
							node.anInt1344 = 0;
						}
					}
					opcode = -1;
					return true;
				}

				/*
				 * Sets interface's model type to 3. (Renders Dialogue
				 * Model/Player Head)
				 */
				if (opcode == 185) {
					int widgetIndex = inBuffer.getUnsignedShortA();
					Widget.cache[widgetIndex].modelType = 3;
					if (Client.localPlayer.npcDefinition == null) {
						Widget.cache[widgetIndex].modelIndex = (Client.localPlayer.appearanceColors[0] << 25)
								+ (Client.localPlayer.appearanceColors[4] << 20)
								+ (Client.localPlayer.appearance[0] << 15) + (Client.localPlayer.appearance[8] << 10)
								+ (Client.localPlayer.appearance[11] << 5) + Client.localPlayer.appearance[1];
					} else {
						Widget.cache[widgetIndex].modelIndex = (int) (0x12345678 + Client.localPlayer.npcDefinition.id);
					}
					opcode = -1;
					return true;
				}

				/* Resets the camera. */
				if (opcode == 107) {
					aBoolean1185 = false;
					for (int i_ = 0; i_ < 5; i_++) {
						aBooleanArray901[i_] = false;
					}
					opcode = -1;
					return true;
				}

				/* Clear Interface Inventory. */
				if (opcode == 72) {
					int widgetIndex = inBuffer.getUnsignedShort();
					Widget widget = Widget.cache[widgetIndex];
					for (int i_ = 0; i_ < widget.items.length; i_++) {
						widget.items[i_] = -1;
						widget.items[i_] = 0;
					}
					opcode = -1;
					return true;
				}

				/* Adds name to the ignore list. */
				if (opcode == 214) {
					ignoreListCount = packetSize / 8;
					for (int i_ = 0; i_ < ignoreListCount; i_++) {
						ignoreList[i_] = inBuffer.getLong();
					}
					opcode = -1;
					return true;
				}

				/* XXX: Figure out usage. */
				if (opcode == 166) {
					aBoolean1185 = true;
					anInt1123 = inBuffer.getUnsignedByte();
					anInt1124 = inBuffer.getUnsignedByte();
					anInt1125 = inBuffer.getUnsignedLEShort();
					anInt1126 = inBuffer.getUnsignedByte();
					anInt1127 = inBuffer.getUnsignedByte();
					if (anInt1127 >= 100) {
						anInt883 = anInt1123 * 128 + 64;
						anInt885 = anInt1124 * 128 + 64;
						anInt884 = method42(currentSceneIndex, anInt885, true, anInt883) - anInt1125;
					}
					opcode = -1;
					return true;
				}

				/* Sets skill information */
				if (opcode == 134) {
					drawTabArea = true;
					int index = inBuffer.getUnsignedByte();
					int experience = inBuffer.getInt2();
					int level = inBuffer.getUnsignedByte();
					skillExperience[index] = experience;
					skillLevel[index] = level;
					skillMaxLevel[index] = 1;
					for (int i_ = 0; i_ < 98; i_++) {
						if (experience >= Client.xpForSkillLevel[i_]) {
							skillMaxLevel[index] = i_ + 2;
						}
					}
					opcode = -1;
					return true;
				}

				/* Sets a sidebar's interface */
				if (opcode == 71) {
					int widgetIndex = inBuffer.getUnsignedLEShort();
					int tabIndex = inBuffer.getUnsignedByteA();
					if (widgetIndex == 65535) {
						widgetIndex = -1;
					}
					tabWidgetIndexes[tabIndex] = widgetIndex;
					drawTabArea = true;
					aBoolean1128 = true;
					opcode = -1;
					return true;
				}

				/* Starts playing a song */
				if (opcode == 74) {
					int index = inBuffer.getUnsignedShort();
					if (index == 65535) {
						index = -1;
					}
					if (index != songIndex && musicEnabled && !Client.lowMemory && songFadeCycle == 0) {
						onDemandRequesterId = index;
						midiFade = true;
						onDemandRequester.request(2, onDemandRequesterId);
					}
					songIndex = index;
					opcode = -1;
					return true;
				}

				/*
				 * Starts playing a sound effect that goes over music, like the
				 * level-up sound.
				 */
				if (opcode == 121) {
					int nextSong = inBuffer.getUnsignedShortA();
					int previousSong = inBuffer.getUnsignedLEShortA();
					if (musicEnabled && !Client.lowMemory) {
						onDemandRequesterId = nextSong;
						midiFade = false;
						onDemandRequester.request(2, onDemandRequesterId);
						songFadeCycle = previousSong;
					}
					opcode = -1;
					return true;
				}

				/* Logs out */
				if (opcode == 109) {
					logout();
					opcode = -1;
					return false;
				}

				/* Sets the position of an interface */
				if (opcode == 70) {
					int x = inBuffer.getShort();
					int y = inBuffer.getForceLEShort();
					int index = inBuffer.getUnsignedShort();
					Widget widget = Widget.cache[index];
					widget.x = x;
					widget.y = y;
					opcode = -1;
					return true;
				}

				/* Loads the map region */
				if (opcode == 73 || opcode == 241) {
					int playerRegionX = anInt1094;
					int playerRegionY = anInt1095;
					if (opcode == 73) {
						playerRegionX = inBuffer.getUnsignedLEShortA();
						playerRegionY = inBuffer.getUnsignedLEShort();
						aBoolean1184 = false;
					}
					if (opcode == 241) {
						playerRegionY = inBuffer.getUnsignedLEShortA();
						inBuffer.initBitAccess();
						for (int plane = 0; plane < 4; plane++) {
							for (int x = 0; x < 13; x++) {
								for (int y = 0; y < 13; y++) {
									int tileExists = inBuffer.getBits(1);
									if (tileExists == 1) {
										constructMapTiles[plane][x][y] = inBuffer.getBits(26);
									} else {
										constructMapTiles[plane][x][y] = -1;
									}
								}
							}
						}
						inBuffer.finishBitAccess();
						playerRegionX = inBuffer.getUnsignedLEShort();
						aBoolean1184 = true;
					}
					if (anInt1094 == playerRegionX && anInt1095 == playerRegionY && anInt1048 == 2) {
						opcode = -1;
						return true;
					}
					anInt1094 = playerRegionX;
					anInt1095 = playerRegionY;
					regionAbsoluteBaseX = (anInt1094 - 6) * 8;
					regionAbsoluteBaseY = (anInt1095 - 6) * 8;
					aBoolean1166 = false;
					if ((anInt1094 / 8 == 48 || anInt1094 / 8 == 49) && anInt1095 / 8 == 48) {
						aBoolean1166 = true;
					}
					if (anInt1094 / 8 == 48 && anInt1095 / 8 == 148) {
						aBoolean1166 = true;
					}
					anInt1048 = 1;
					aLong849 = System.currentTimeMillis();
					currentSceneBuffer.createRasterizer();
					fontNormal.drawString("Loading - please wait.", 257, 151, 0);
					fontNormal.drawString("Loading - please wait.", 256, 150, 0xFFFFFF);
					currentSceneBuffer.drawGraphics(4, 4, gameGraphics);
					if (opcode == 73) {
						int i_1056_ = 0;
						for (int i_1057_ = (anInt1094 - 6) / 8; i_1057_ <= (anInt1094 + 6) / 8; i_1057_++) {
							for (int i_1058_ = (anInt1095 - 6) / 8; i_1058_ <= (anInt1095 + 6) / 8; i_1058_++) {
								i_1056_++;
							}
						}
						aByteArrayArray1208 = new byte[i_1056_][];
						aByteArrayArray1272 = new byte[i_1056_][];
						anIntArray1259 = new int[i_1056_];
						anIntArray1260 = new int[i_1056_];
						anIntArray1261 = new int[i_1056_];
						i_1056_ = 0;
						for (int i_1059_ = (anInt1094 - 6) / 8; i_1059_ <= (anInt1094 + 6) / 8; i_1059_++) {
							for (int i_1060_ = (anInt1095 - 6) / 8; i_1060_ <= (anInt1095 + 6) / 8; i_1060_++) {
								anIntArray1259[i_1056_] = (i_1059_ << 8) + i_1060_;
								if (aBoolean1166
										&& (i_1060_ == 49 || i_1060_ == 149 || i_1060_ == 147 || i_1059_ == 50 || i_1059_ == 49
												&& i_1060_ == 47)) {
									anIntArray1260[i_1056_] = -1;
									anIntArray1261[i_1056_] = -1;
									i_1056_++;
								} else {
									int i_1061_ = anIntArray1260[i_1056_] = onDemandRequester.regIndex(0, i_1060_,
											i_1059_);
									if (i_1061_ != -1) {
										onDemandRequester.request(3, i_1061_);
									}
									int i_1062_ = anIntArray1261[i_1056_] = onDemandRequester.regIndex(1, i_1060_,
											i_1059_);
									if (i_1062_ != -1) {
										onDemandRequester.request(3, i_1062_);
									}
									i_1056_++;
								}
							}
						}
					}

					/* Construct region block (8x8 block) */
					if (opcode == 241) {
						int i_1063_ = 0;
						int[] is = new int[676];
						for (int i_1064_ = 0; i_1064_ < 4; i_1064_++) {
							for (int i_1065_ = 0; i_1065_ < 13; i_1065_++) {
								for (int i_1066_ = 0; i_1066_ < 13; i_1066_++) {
									int i_1067_ = constructMapTiles[i_1064_][i_1065_][i_1066_];
									if (i_1067_ != -1) {
										int i_1068_ = i_1067_ >> 14 & 0x3ff;
										int i_1069_ = i_1067_ >> 3 & 0x7ff;
										int i_1070_ = (i_1068_ / 8 << 8) + i_1069_ / 8;
										for (int i_1071_ = 0; i_1071_ < i_1063_; i_1071_++) {
											if (is[i_1071_] == i_1070_) {
												i_1070_ = -1;
												break;
											}
										}
										if (i_1070_ != -1) {
											is[i_1063_++] = i_1070_;
										}
									}
								}
							}
						}
						aByteArrayArray1208 = new byte[i_1063_][];
						aByteArrayArray1272 = new byte[i_1063_][];
						anIntArray1259 = new int[i_1063_];
						anIntArray1260 = new int[i_1063_];
						anIntArray1261 = new int[i_1063_];
						for (int i_1072_ = 0; i_1072_ < i_1063_; i_1072_++) {
							int i_1073_ = anIntArray1259[i_1072_] = is[i_1072_];
							int i_1074_ = i_1073_ >> 8 & 0xff;
							int i_1075_ = i_1073_ & 0xff;
							int i_1076_ = anIntArray1260[i_1072_] = onDemandRequester.regIndex(0, i_1075_, i_1074_);
							if (i_1076_ != -1) {
								onDemandRequester.request(3, i_1076_);
							}
							int i_1077_ = anIntArray1261[i_1072_] = onDemandRequester.regIndex(1, i_1075_, i_1074_);
							if (i_1077_ != -1) {
								onDemandRequester.request(3, i_1077_);
							}
						}
					}
					int i_1078_ = regionAbsoluteBaseX - anInt1061;
					int i_1079_ = regionAbsoluteBaseY - anInt1062;
					anInt1061 = regionAbsoluteBaseX;
					anInt1062 = regionAbsoluteBaseY;
					for (int i_1080_ = 0; i_1080_ < 16384; i_1080_++) {
						Npc npc = actors[i_1080_];
						if (npc != null) {
							for (int i_1081_ = 0; i_1081_ < 10; i_1081_++) {
								npc.pathX[i_1081_] -= i_1078_;
								npc.pathY[i_1081_] -= i_1079_;
							}
							npc.xWithBoundary -= i_1078_ * 128;
							npc.yWithBoundary -= i_1079_ * 128;
						}
					}
					for (int i_1082_ = 0; i_1082_ < MAX_PLAYERS; i_1082_++) {
						Player player = players[i_1082_];
						if (player != null) {
							for (int i_1083_ = 0; i_1083_ < 10; i_1083_++) {
								player.pathX[i_1083_] -= i_1078_;
								player.pathY[i_1083_] -= i_1079_;
							}
							player.xWithBoundary -= i_1078_ * 128;
							player.yWithBoundary -= i_1079_ * 128;
						}
					}
					aBoolean1105 = true;
					int i_1084_ = 0;
					int i_1085_ = 104;
					int i_1086_ = 1;
					if (i_1078_ < 0) {
						i_1084_ = 103;
						i_1085_ = -1;
						i_1086_ = -1;
					}
					int i_1087_ = 0;
					int i_1088_ = 104;
					int i_1089_ = 1;
					if (i_1079_ < 0) {
						i_1087_ = 103;
						i_1088_ = -1;
						i_1089_ = -1;
					}
					for (int i_1090_ = i_1084_; i_1090_ != i_1085_; i_1090_ += i_1086_) {
						for (int i_1091_ = i_1087_; i_1091_ != i_1088_; i_1091_ += i_1089_) {
							int i_1092_ = i_1090_ + i_1078_;
							int i_1093_ = i_1091_ + i_1079_;
							for (int i_1094_ = 0; i_1094_ < 4; i_1094_++) {
								if (i_1092_ >= 0 && i_1093_ >= 0 && i_1092_ < 104 && i_1093_ < 104) {
									groundItemNodes[i_1094_][i_1090_][i_1091_] = groundItemNodes[i_1094_][i_1092_][i_1093_];
								} else {
									groundItemNodes[i_1094_][i_1090_][i_1091_] = null;
								}
							}
						}
					}
					for (SpawnObjectNode spawnobjectnode = (SpawnObjectNode) aLinkedList1204.getBack(); spawnobjectnode != null; spawnobjectnode = (SpawnObjectNode) aLinkedList1204
							.getPrevious()) {
						spawnobjectnode.x -= i_1078_;
						spawnobjectnode.y -= i_1079_;
						if (spawnobjectnode.x < 0 || spawnobjectnode.y < 0 || spawnobjectnode.x >= 104
								|| spawnobjectnode.y >= 104) {
							spawnobjectnode.remove();
						}
					}
					if (destinationX != 0) {
						destinationX -= i_1078_;
						destinationY -= i_1079_;
					}
					aBoolean1185 = false;
					opcode = -1;
					return true;
				}

				/* Sets the walkable interface. */
				if (opcode == 208) {
					int widgetIndex = inBuffer.getForceLEShort();
					if (widgetIndex >= 0) {
						method60(widgetIndex);
					}
					walkablewidgetIndex = widgetIndex;
					opcode = -1;
					return true;
				}

				/* Sets the minimap state */
				if (opcode == 99) {
					minimapState = inBuffer.getUnsignedByte();
					opcode = -1;
					return true;
				}

				/* Sets interface's model type to 2. (Draws Actor head model) */
				if (opcode == 75) {
					int actorIndex = inBuffer.getUnsignedShortA();
					int widgetIndex = inBuffer.getUnsignedShortA();
					Widget.cache[widgetIndex].modelType = 2;
					Widget.cache[widgetIndex].modelIndex = actorIndex;
					opcode = -1;
					return true;
				}

				/* Sets the system update time. */
				if (opcode == 114) {
					systemUpdateTime = inBuffer.getUnsignedShort() * 30;
					opcode = -1;
					return true;
				}

				/* TODO: Figure out usage. */
				if (opcode == 60) {
					playerPositionY = inBuffer.getUnsignedByte();
					playerPositionX = inBuffer.getUnsignedByteC();
					while (inBuffer.offset < packetSize) {
						int i_1098_ = inBuffer.getUnsignedByte();
						method137(anInt1144, inBuffer, i_1098_);
					}
					opcode = -1;
					return true;
				}

				/* Shakes the camera around like an earthquake. */
				if (opcode == 35) {
					int type = inBuffer.getUnsignedByte();
					int jitter = inBuffer.getUnsignedByte();
					int amplitude = inBuffer.getUnsignedByte();
					int frequency = inBuffer.getUnsignedByte();
					/*
					 * Type 0: Shake horizontally Type 1: Shake inward Type 2:
					 * Shake vertically Type 3: Shake yaw Type 4: Shake pitch
					 */
					aBooleanArray901[type] = true;
					anIntArray898[type] = jitter;
					anIntArray1228[type] = amplitude;
					anIntArray953[type] = frequency;
					anIntArray1055[type] = 0;
					opcode = -1;
					return true;
				}

				/* Adds a track? */
				if (opcode == 174) {
					int index = inBuffer.getUnsignedLEShort();
					int loop = inBuffer.getUnsignedByte();
					int delay = inBuffer.getUnsignedLEShort();
					if (aBoolean873 && !Client.lowMemory && trackCount < 50) {
						trackIndexes[trackCount] = index;
						trackLoop[trackCount] = loop;
						trackDelay[trackCount] = delay + SoundTrack.trackDelays[index];
						trackCount++;
					}
					opcode = -1;
					return true;
				}

				/* Adds an action to players */
				// TODO: Figure out what unknownVar1 is.
				if (opcode == 104) {
					int actionIndex = inBuffer.getUnsignedByteC();
					int unknownVar1 = inBuffer.getUnsignedByteA();
					String action = inBuffer.getString();
					if (actionIndex >= 1 && actionIndex <= 5) {
						if (action.equalsIgnoreCase("null")) {
							action = null;
						}
						playerActions[actionIndex - 1] = action;
						aBooleanArray1153[actionIndex - 1] = unknownVar1 == 0;
					}
					opcode = -1;
					return true;
				}

				/* Resets your destination. */
				if (opcode == 78) {
					destinationX = 0;
					opcode = -1;
					return true;
				}

				/* Puts a message in the chat box. */
				if (opcode == 253) {
					String s = inBuffer.getString();
					if (s.endsWith(":tradereq:")) {
						String name = s.substring(0, s.indexOf(":"));
						long nameLong = TextUtils.nameToLong(name);
						boolean ignored = false;
						for (int i_ = 0; i_ < ignoreListCount; i_++) {
							if (ignoreList[i_] == nameLong) {
								ignored = true;
								break;
							}
						}
						if (!ignored && anInt1276 == 0) {
							sendMessage("wishes to trade with you.", 4, name);
						}
					} else if (s.endsWith(":duelreq:")) {
						String name = s.substring(0, s.indexOf(":"));
						long nameLong = TextUtils.nameToLong(name);
						boolean ignored = false;
						for (int i_ = 0; i_ < ignoreListCount; i_++) {
							if (ignoreList[i_] == nameLong) {
								ignored = true;
								break;
							}
						}
						if (!ignored && anInt1276 == 0) {
							sendMessage("wishes to duel with you.", 8, name);
						}
					} else if (s.endsWith(":chalreq:")) {
						String name = s.substring(0, s.indexOf(":"));
						long nameLong = TextUtils.nameToLong(name);
						boolean ignored = false;
						for (int i_ = 0; i_ < ignoreListCount; i_++) {
							if (ignoreList[i_] == nameLong) {
								ignored = true;
								break;
							}
						}
						if (!ignored && anInt1276 == 0) {
							// Nobody uses this anyway, but in-case they do; I
							// made it send this message rather than just
							// ':chalreq:'.
							sendMessage("has challenged you.", 8, name);
							// String trimmed = s.substring(s.indexOf(":") + 1,
							// s.length() - 9);
							// sendMessage(trimmed, 8, name);
						}
					} else {
						sendMessage(s, 0, "");
					}
					opcode = -1;
					return true;
				}

				/* Resets all animations for the Players and Actors in the area. */
				if (opcode == 1) {
					for (int i_ = 0; i_ < players.length; i_++) {
						if (players[i_] != null) {
							players[i_].animation = -1;
						}
					}
					for (int i_ = 0; i_ < actors.length; i_++) {
						if (actors[i_] != null) {
							actors[i_].animation = -1;
						}
					}
					opcode = -1;
					return true;
				}

				/* Updates the friends list information. */
				if (opcode == 50) {
					long nameLong = inBuffer.getLong();
					int worldIndex = inBuffer.getUnsignedByte();
					String name = TextUtils.formatName(TextUtils.longToName(nameLong));
					for (int i_ = 0; i_ < friendsListCount; i_++) {
						if (nameLong == friendsListLongs[i_]) {
							if (friendsListWorlds[i_] != worldIndex) {
								friendsListWorlds[i_] = worldIndex;
								drawTabArea = true;
								if (worldIndex > 0) {
									sendMessage(name + " has logged in.", 5, "");
								}
								if (worldIndex == 0) {
									sendMessage(name + " has logged out.", 5, "");
								}
							}
							name = null;
							break;
						}
					}
					if (name != null && friendsListCount < 200) {
						friendsListLongs[friendsListCount] = nameLong;
						friendsListNames[friendsListCount] = name;
						friendsListWorlds[friendsListCount] = worldIndex;
						friendsListCount++;
						drawTabArea = true;
					}
					boolean bool_1122_ = false;
					while (!bool_1122_) {
						bool_1122_ = true;
						for (int i_1123_ = 0; i_1123_ < friendsListCount - 1; i_1123_++) {
							if (friendsListWorlds[i_1123_] != Client.nodeIndex
									&& friendsListWorlds[i_1123_ + 1] == Client.nodeIndex
									|| friendsListWorlds[i_1123_] == 0 && friendsListWorlds[i_1123_ + 1] != 0) {
								int i_1124_ = friendsListWorlds[i_1123_];
								friendsListWorlds[i_1123_] = friendsListWorlds[i_1123_ + 1];
								friendsListWorlds[i_1123_ + 1] = i_1124_;
								String string_1125_ = friendsListNames[i_1123_];
								friendsListNames[i_1123_] = friendsListNames[i_1123_ + 1];
								friendsListNames[i_1123_ + 1] = string_1125_;
								long l_1126_ = friendsListLongs[i_1123_];
								friendsListLongs[i_1123_] = friendsListLongs[i_1123_ + 1];
								friendsListLongs[i_1123_ + 1] = l_1126_;
								drawTabArea = true;
								bool_1122_ = false;
							}
						}
					}
					opcode = -1;
					return true;
				}

				/* Updates your run energy. */
				if (opcode == 110) {
					if (currentTabIndex == 12) {
						drawTabArea = true;
					}
					playerEnergy = inBuffer.getUnsignedByte();
					opcode = -1;
					return true;
				}

				/* Marks an Actor with a hint icon. */
				if (opcode == 254) {
					hintIconType = inBuffer.getUnsignedByte();
					if (hintIconType == 1) {
						hintIconActorIndex = inBuffer.getUnsignedLEShort();
					}
					if (hintIconType >= 2 && hintIconType <= 6) {
						if (hintIconType == 2) {
							anInt962 = 64;
							anInt963 = 64;
						}
						if (hintIconType == 3) {
							anInt962 = 0;
							anInt963 = 64;
						}
						if (hintIconType == 4) {
							anInt962 = 128;
							anInt963 = 64;
						}
						if (hintIconType == 5) {
							anInt962 = 64;
							anInt963 = 0;
						}
						if (hintIconType == 6) {
							anInt962 = 64;
							anInt963 = 128;
						}
						hintIconType = 2;
						hintIconX = inBuffer.getUnsignedLEShort();
						hintIconY = inBuffer.getUnsignedLEShort();
						hintIconOffset = inBuffer.getUnsignedByte();
					}
					if (hintIconType == 10) {
						hintIconPlayerIndex = inBuffer.getUnsignedLEShort();
					}
					opcode = -1;
					return true;
				}

				/* Draws an interface over an inventory interface. */
				if (opcode == 248) {
					int widgetIndex = inBuffer.getUnsignedLEShortA();
					int inventorywidgetIndex = inBuffer.getUnsignedLEShort();
					if (chatboxWidgetIndex != -1) {
						chatboxWidgetIndex = -1;
						aBoolean1248 = true;
					}
					if (inputType != 0) {
						inputType = 0;
						aBoolean1248 = true;
					}
					openWidgetIndex = widgetIndex;
					anInt1214 = inventorywidgetIndex;
					drawTabArea = true;
					aBoolean1128 = true;
					aBoolean1174 = false;
					opcode = -1;
					return true;
				}

				/* Sets the scroll position of an interface. */
				if (opcode == 79) {
					int widgetIndex = inBuffer.getUnsignedShort();
					int scrollPosition = inBuffer.getUnsignedLEShortA();
					Widget widget = Widget.cache[widgetIndex];
					if (widget != null && widget.type == 0) {
						if (scrollPosition < 0) {
							scrollPosition = 0;
						}
						if (scrollPosition > widget.scrollLimit - widget.height) {
							scrollPosition = widget.scrollLimit - widget.height;
						}
						widget.scrollPosition = scrollPosition;
					}
					opcode = -1;
					return true;
				}

				/* Resets all settings. */
				if (opcode == 68) {
					for (int i_ = 0; i_ < settings.length; i_++) {
						if (settings[i_] != defaultSettings[i_]) {
							settings[i_] = defaultSettings[i_];
							method33(i_);
							drawTabArea = true;
						}
					}
					opcode = -1;
					return true;
				}

				/* Shows a private message. */
				if (opcode == 196) {
					long nameLong = inBuffer.getLong();
					int i_1132_ = inBuffer.getInt();
					int senderPlayerRights = inBuffer.getUnsignedByte();
					boolean bool_1134_ = false;
					for (int i_1135_ = 0; i_1135_ < 100; i_1135_++) {
						if (anIntArray1265[i_1135_] == i_1132_) {
							bool_1134_ = true;
							break;
						}
					}
					if (senderPlayerRights <= 1) {
						for (int i_1136_ = 0; i_1136_ < ignoreListCount; i_1136_++) {
							if (ignoreList[i_1136_] == nameLong) {
								bool_1134_ = true;
								break;
							}
						}
					}
					if (!bool_1134_ && anInt1276 == 0) {
						try {
							anIntArray1265[anInt1194] = i_1132_;
							anInt1194 = (anInt1194 + 1) % 100;
							String string = ChatEncoder.get(packetSize - 13, inBuffer);
							if (senderPlayerRights != 3) {
								string = ChatCensor.censorString(string);
							}
							if (senderPlayerRights == 2 || senderPlayerRights == 3) {
								sendMessage(string, 7, "@cr2@" + TextUtils.formatName(TextUtils.longToName(nameLong)));
							} else if (senderPlayerRights == 1) {
								sendMessage(string, 7, "@cr1@" + TextUtils.formatName(TextUtils.longToName(nameLong)));
							} else {
								sendMessage(string, 3, TextUtils.formatName(TextUtils.longToName(nameLong)));
							}
						} catch (Exception exception) {
							SignLink.reportError("cde1");
						}
					}
					opcode = -1;
					return true;
				}

				/* Sets the local player's region position. */
				if (opcode == 85) {
					playerPositionY = inBuffer.getUnsignedByteC();
					playerPositionX = inBuffer.getUnsignedByteC();
					opcode = -1;
					return true;
				}

				/* Flashes a sidebar icon. */
				if (opcode == 24) {
					flashingSidebar = inBuffer.getUnsignedByteS();
					if (flashingSidebar == currentTabIndex) {
						if (flashingSidebar == 3) {
							currentTabIndex = 1;
						} else {
							currentTabIndex = 3;
						}
						drawTabArea = true;
					}
					opcode = -1;
					return true;
				}

				/* Displays an item model within an interface. */
				if (opcode == 246) {
					int widgetIndex = inBuffer.getUnsignedShort();
					int itemModelZoom = inBuffer.getUnsignedLEShort();
					int itemIndex = inBuffer.getUnsignedLEShort();
					if (itemIndex == 65535) {
						Widget.cache[widgetIndex].modelType = 0;
						opcode = -1;
						return true;
					}
					ItemDefinition itemdefinition = ItemDefinition.getDefinition(itemIndex);
					Widget.cache[widgetIndex].modelType = 4;
					Widget.cache[widgetIndex].modelIndex = itemIndex;
					Widget.cache[widgetIndex].rotationX = itemdefinition.modelRotation1;
					Widget.cache[widgetIndex].rotationY = itemdefinition.modelRotation2;
					Widget.cache[widgetIndex].zoom = itemdefinition.modelZoom * 100 / itemModelZoom;
					opcode = -1;
					return true;
				}

				/* Sets whether an interface is hidden until hovered. */
				if (opcode == 171) {
					boolean hiddenUntilHovered = inBuffer.getUnsignedByte() == 1;
					int widgetIndex = inBuffer.getUnsignedLEShort();
					Widget.cache[widgetIndex].hiddenUntilHovered = hiddenUntilHovered;
					opcode = -1;
					return true;
				}

				/* TODO: Figure out usage. */
				if (opcode == 142) {
					int i_1142_ = inBuffer.getUnsignedShort();
					method60(i_1142_);
					if (chatboxWidgetIndex != -1) {
						chatboxWidgetIndex = -1;
						aBoolean1248 = true;
					}
					if (inputType != 0) {
						inputType = 0;
						aBoolean1248 = true;
					}
					anInt1214 = i_1142_;
					drawTabArea = true;
					aBoolean1128 = true;
					openWidgetIndex = -1;
					aBoolean1174 = false;
					opcode = -1;
					return true;
				}

				/* Sets an interface's disabled text. */
				if (opcode == 126) {
					String text = inBuffer.getString();
					int widgetIndex = inBuffer.getUnsignedLEShortA();
					Widget.cache[widgetIndex].disabledText = text;
					if (Widget.cache[widgetIndex].parentIndex == tabWidgetIndexes[currentTabIndex]) {
						drawTabArea = true;
					}
					opcode = -1;
					return true;
				}

				/* Sets the chat settings. */
				if (opcode == 206) {
					publicChatSetting = inBuffer.getUnsignedByte();
					privateChatSetting = inBuffer.getUnsignedByte();
					tradeSetting = inBuffer.getUnsignedByte();
					aBoolean1258 = true;
					aBoolean1248 = true;
					opcode = -1;
					return true;
				}

				/* Sets the player's carried weight. */
				if (opcode == 240) {
					if (currentTabIndex == 12) {
						drawTabArea = true;
					}
					playerWeight = inBuffer.getShort();
					opcode = -1;
					return true;
				}

				/* Sets an interface to draw a model. */
				if (opcode == 8) {
					int widgetIndex = inBuffer.getUnsignedShortA();
					int interfaceModelId = inBuffer.getUnsignedLEShort();
					Widget.cache[widgetIndex].modelType = 1;
					Widget.cache[widgetIndex].modelIndex = interfaceModelId;
					opcode = -1;
					return true;
				}

				/* Sets an interface's disabled color. */
				if (opcode == 122) {
					int widgetIndex = inBuffer.getUnsignedShortA();
					int rgb = inBuffer.getUnsignedShortA();
					int red = rgb >> 10 & 0x1F;
					int green = rgb >> 5 & 0x1F;
					int blue = rgb & 0x1F;
					Widget.cache[widgetIndex].disabledColor = (red << 19) + (green << 11) + (blue << 3);
					opcode = -1;
					return true;
				}

				/* TODO: Sets the stack sizes in an inventory. */
				if (opcode == 53) {
					drawTabArea = true;
					int widgetIndex = inBuffer.getUnsignedLEShort();
					Widget widget = Widget.cache[widgetIndex];
					int itemCount = inBuffer.getUnsignedLEShort();
					for (int i_ = 0; i_ < itemCount; i_++) {
						int stackSize = inBuffer.getUnsignedByte();
						if (stackSize == 255) {
							stackSize = inBuffer.getInt1();
						}
						widget.items[i_] = inBuffer.getUnsignedShortA();
						widget.itemAmounts[i_] = stackSize;
					}
					for (int i_ = itemCount; i_ < widget.items.length; i_++) {
						widget.items[i_] = 0;
						widget.itemAmounts[i_] = 0;
					}
					opcode = -1;
					return true;
				}

				/* Sets the rotation of a model in an interface. */
				if (opcode == 230) {
					int modelZoom = inBuffer.getUnsignedLEShortA();
					int widgetIndex = inBuffer.getUnsignedLEShort();
					int modelRotationX = inBuffer.getUnsignedLEShort();
					int modelRotationY = inBuffer.getUnsignedShortA();
					Widget.cache[widgetIndex].rotationX = modelRotationX;
					Widget.cache[widgetIndex].rotationY = modelRotationY;
					Widget.cache[widgetIndex].zoom = modelZoom;
					opcode = -1;
					return true;
				}

				/* Sets the friends list status. */
				if (opcode == 221) {
					friendListStatus = inBuffer.getUnsignedByte();
					drawTabArea = true;
					opcode = -1;
					return true;
				}

				/* TODO: Figure out usage. */
				if (opcode == 177) {
					aBoolean1185 = true;
					anInt1020 = inBuffer.getUnsignedByte();
					anInt1021 = inBuffer.getUnsignedByte();
					anInt1022 = inBuffer.getUnsignedLEShort();
					anInt1023 = inBuffer.getUnsignedByte();
					anInt1024 = inBuffer.getUnsignedByte();
					if (anInt1024 >= 100) {
						int i_1160_ = anInt1020 * 128 + 64;
						int i_1161_ = anInt1021 * 128 + 64;
						int i_1162_ = method42(currentSceneIndex, i_1161_, true, i_1160_) - anInt1022;
						int i_1163_ = i_1160_ - anInt883;
						int i_1164_ = i_1162_ - anInt884;
						int i_1165_ = i_1161_ - anInt885;
						int i_1166_ = (int) Math.sqrt(i_1163_ * i_1163_ + i_1165_ * i_1165_);
						anInt886 = (int) (Math.atan2(i_1164_, i_1166_) * 325.949) & 0x7ff;
						anInt887 = (int) (Math.atan2(i_1163_, i_1165_) * -325.949) & 0x7ff;
						if (anInt886 < 128) {
							anInt886 = 128;
						}
						if (anInt886 > 383) {
							anInt886 = 383;
						}
					}
					opcode = -1;
					return true;
				}

				/* Initialize Player */
				if (opcode == 249) {
					anInt1071 = inBuffer.getUnsignedByteA();
					anInt909 = inBuffer.getUnsignedShortA();
					opcode = -1;
					return true;
				}

				/* Updates Actors */
				if (opcode == 65) {
					updateActors(inBuffer, packetSize);
					opcode = -1;
					return true;
				}

				/* Opens the 'Enter Amount' input. */
				if (opcode == 27) {
					aBoolean1281 = false;
					inputType = 1;
					inputInputMessage = "";
					aBoolean1248 = true;
					opcode = -1;
					return true;
				}

				/* Opens the 'Enter Name' input. */
				if (opcode == 187) {
					aBoolean1281 = false;
					inputType = 2;
					inputInputMessage = "";
					aBoolean1248 = true;
					opcode = -1;
					return true;
				}

				/* Opens an interface. */
				if (opcode == 97) {
					int widgetIndex = inBuffer.getUnsignedLEShort();
					method60(widgetIndex);
					if (anInt1214 != -1) {
						anInt1214 = -1;
						drawTabArea = true;
						aBoolean1128 = true;
					}
					if (chatboxWidgetIndex != -1) {
						chatboxWidgetIndex = -1;
						aBoolean1248 = true;
					}
					if (inputType != 0) {
						inputType = 0;
						aBoolean1248 = true;
					}
					openWidgetIndex = widgetIndex;
					aBoolean1174 = false;
					opcode = -1;
					return true;
				}

				/* TODO: Figure out usage. */
				if (opcode == 218) {
					int i_1168_ = inBuffer.getForceLEShortA();
					anInt1067 = i_1168_;
					aBoolean1248 = true;
					opcode = -1;
					return true;
				}

				/* Sets a setting's value */
				if (opcode == 87) {
					int settingIndex = inBuffer.getUnsignedShort();
					int settingValue = inBuffer.getInt2();
					defaultSettings[settingIndex] = settingValue;
					if (settings[settingIndex] != settingValue) {
						settings[settingIndex] = settingValue;
						method33(settingIndex);
						drawTabArea = true;
						if (anInt1067 != -1) {
							aBoolean1248 = true;
						}
					}
					opcode = -1;
					return true;
				}

				/* Sets a client setting and changes the default value to this. */
				if (opcode == 36) {
					int i_1171_ = inBuffer.getUnsignedShort();
					int i_1172_ = inBuffer.get();
					defaultSettings[i_1171_] = i_1172_;
					if (settings[i_1171_] != i_1172_) {
						settings[i_1171_] = i_1172_;
						method33(i_1171_);
						drawTabArea = true;
						if (anInt1067 != -1) {
							aBoolean1248 = true;
						}
					}
					opcode = -1;
					return true;
				}

				/* TODO: Figure out usage. */
				if (opcode == 61) {
					anInt1080 = inBuffer.getUnsignedByte();
					opcode = -1;
					return true;
				}

				/* Sets the animation for the model within an interface. */
				if (opcode == 200) {
					int widgetIndex = inBuffer.getUnsignedLEShort();
					int animationId = inBuffer.getShort();
					Widget widget = Widget.cache[widgetIndex];
					widget.disabledAnimation = animationId;
					if (animationId == -1) {
						widget.animationFrame = 0;
						widget.animationDuration = 0;
					}
					opcode = -1;
					return true;
				}

				/* Closes all open interfaces. */
				if (opcode == 219) {
					if (anInt1214 != -1) {
						anInt1214 = -1;
						drawTabArea = true;
						aBoolean1128 = true;
					}
					if (chatboxWidgetIndex != -1) {
						chatboxWidgetIndex = -1;
						aBoolean1248 = true;
					}
					if (inputType != 0) {
						inputType = 0;
						aBoolean1248 = true;
					}
					openWidgetIndex = -1;
					aBoolean1174 = false;
					opcode = -1;
					return true;
				}

				/* Sets an interface's inventory array. */
				if (opcode == 34) {
					drawTabArea = true;
					int widgetIndex = inBuffer.getUnsignedLEShort();
					Widget widget = Widget.cache[widgetIndex];
					while (inBuffer.offset < packetSize) {
						int inventoryIndex = inBuffer.getSmartB();
						int itemIndex = inBuffer.getUnsignedLEShort();
						int itemAmount = inBuffer.getUnsignedByte();
						if (itemAmount == 255) {
							itemAmount = inBuffer.getInt();
						}
						if (inventoryIndex >= 0 && inventoryIndex < widget.items.length) {
							widget.items[inventoryIndex] = itemIndex;
							widget.itemAmounts[inventoryIndex] = itemAmount;
						}
					}
					opcode = -1;
					return true;
				}
				if (opcode == 105 || opcode == 84 || opcode == 147 || opcode == 215 || opcode == 4 || opcode == 117
						|| opcode == 156 || opcode == 44 || opcode == 160 || opcode == 101 || opcode == 151) {
					method137(anInt1144, inBuffer, opcode);
					opcode = -1;
					return true;
				}
				if (opcode == 106) {
					currentTabIndex = inBuffer.getUnsignedByteC();
					drawTabArea = true;
					aBoolean1128 = true;
					opcode = -1;
					return true;
				}
				if (opcode == 164) {
					int i_1179_ = inBuffer.getUnsignedShort();
					method60(i_1179_);
					if (anInt1214 != -1) {
						anInt1214 = -1;
						drawTabArea = true;
						aBoolean1128 = true;
					}
					chatboxWidgetIndex = i_1179_;
					aBoolean1248 = true;
					openWidgetIndex = -1;
					aBoolean1174 = false;
					opcode = -1;
					return true;
				}
				SignLink.reportError("T1 - " + opcode + "," + packetSize + " - " + anInt867 + "," + anInt868);
				logout();
			} catch (IOException ioexception) {
				method68(-670);
			} catch (Exception exception) {
				String string = "T2 - " + opcode + "," + anInt867 + "," + anInt868 + " - " + packetSize + ","
						+ (regionAbsoluteBaseX + Client.localPlayer.pathX[0]) + ","
						+ (regionAbsoluteBaseY + Client.localPlayer.pathY[0]) + " - ";
				for (int i = 0; i < packetSize && i < 50; i++) {
					string += inBuffer.payload[i] + ",";
				}
				SignLink.reportError(string);
				logout();
			}
			return true;
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("32862, " + bool + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void method146(byte b) {
		try {
			anInt1290++;
			method47(0, true);
			method26(true);
			method47(0, false);
			method26(false);
			method55(-948);
			method104(true);
			if (!aBoolean1185) {
				int i = anInt1209;
				if (anInt1009 / 256 > i) {
					i = anInt1009 / 256;
				}
				if (aBooleanArray901[4] && anIntArray1228[4] + 128 > i) {
					i = anIntArray1228[4] + 128;
				}
				int i_1180_ = anInt1210 + anInt921 & 0x7ff;
				method144(
						0,
						600 + i * 3,
						i,
						anInt1039,
						method42(currentSceneIndex, Client.localPlayer.yWithBoundary, true,
								Client.localPlayer.xWithBoundary) - 50, i_1180_, anInt1040);
			}
			int i;
			if (!aBoolean1185) {
				i = method120();
			} else {
				i = method121(anInt1106);
			}
			int i_1181_ = anInt883;
			int i_1182_ = anInt884;
			int i_1183_ = anInt885;
			int i_1184_ = anInt886;
			int i_1185_ = anInt887;
			for (int i_1186_ = 0; i_1186_ < 5; i_1186_++) {
				if (aBooleanArray901[i_1186_]) {
					int i_1187_ = (int) (Math.random() * (anIntArray898[i_1186_] * 2 + 1) - anIntArray898[i_1186_] + Math
							.sin(anIntArray1055[i_1186_] * (anIntArray953[i_1186_] / 100.0)) * anIntArray1228[i_1186_]);
					if (i_1186_ == 0) {
						anInt883 += i_1187_;
					}
					if (i_1186_ == 1) {
						anInt884 += i_1187_;
					}
					if (i_1186_ == 2) {
						anInt885 += i_1187_;
					}
					if (i_1186_ == 3) {
						anInt887 = anInt887 + i_1187_ & 0x7ff;
					}
					if (i_1186_ == 4) {
						anInt886 += i_1187_;
						if (anInt886 < 128) {
							anInt886 = 128;
						}
						if (anInt886 > 383) {
							anInt886 = 383;
						}
					}
				}
			}
			int i_1188_ = Rasterizer3D.anInt1501;
			Model.aBoolean1677 = true;
			if (b == 1) {
				Model.anInt1680 = 0;
				Model.anInt1678 = mouseEventX - 4;
				Model.anInt1679 = mouseEventY - 4;
				Rasterizer.resetPixels();
				currentScene.method535(anInt883, anInt885, anInt887, anInt884, i, anInt886, false);
				currentScene.method510((byte) 104);
				method34();
				method61();
				method37(854, i_1188_);
				method112();
				currentSceneBuffer.drawGraphics(4, 4, gameGraphics);
				anInt883 = i_1181_;
				anInt884 = i_1182_;
				anInt885 = i_1183_;
				anInt886 = i_1184_;
				anInt887 = i_1185_;
			}
		} catch (RuntimeException runtimeexception) {
			SignLink.reportError("97263, " + b + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public final void closeWidgets() {
		do {
			try {
				outBuffer.putOpcode(130);
				if (anInt1214 != -1) {
					anInt1214 = -1;
					drawTabArea = true;
					aBoolean1174 = false;
					aBoolean1128 = true;
				}
				if (chatboxWidgetIndex != -1) {
					chatboxWidgetIndex = -1;
					aBoolean1248 = true;
					aBoolean1174 = false;
				}
				openWidgetIndex = -1;
			} catch (RuntimeException runtimeexception) {
				SignLink.reportError("33125, " + runtimeexception.toString());
				throw new RuntimeException();
			}
			break;
		} while (false);
	}

	@Override
	public void onRepaint() {
		drawGameframe = true;
	}

	static {
		int i = 0;
		for (int level = 0; level < 99; level++) {
			int realLevel = level + 1;
			int expDiff = (int) (realLevel + 300.0 * Math.pow(2.0, realLevel / 7.0));
			i += expDiff;
			Client.xpForSkillLevel[level] = i / 4;
		}
		Client.RSA_EXPONENT = new BigInteger(
				"58778699976184461502525193738213253649000149147835990136706041084440742975821");
		Client.VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\u00a3$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
		anIntArray1229 = new int[] { 9104, 10275, 7595, 3610, 7975, 8526, 918, 38802, 24466, 10145, 58654, 5027, 1457,
				16565, 34991, 25486 };
		Client.BITFIELD_MAX_VALUE = new int[32];
		i = 2;
		for (int i_1192_ = 0; i_1192_ < 32; i_1192_++) {
			Client.BITFIELD_MAX_VALUE[i_1192_] = i - 1;
			i += i;
		}
	}
}