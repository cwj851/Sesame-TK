package tkaxv7s.xposed.sesame.util;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.Data;
import tkaxv7s.xposed.sesame.data.ModelTask;
import tkaxv7s.xposed.sesame.model.task.antForest.AntForestV2;

import java.io.File;
import java.util.*;

@Data
public class Status {

    private static final String TAG = Status.class.getSimpleName();

    public static final Status INSTANCE = new Status();

    // forest
    private ArrayList<WaterFriendLog> waterFriendLogList = new ArrayList<>();
    private ArrayList<String> cooperateWaterList = new ArrayList<>();
    private ArrayList<String> syncStepList = new ArrayList<>();
    private ArrayList<ReserveLog> reserveLogList = new ArrayList<>();
    private ArrayList<BeachLog> beachLogList = new ArrayList<>();
    private ArrayList<String> beachTodayList = new ArrayList<>();
    private ArrayList<String> ancientTreeCityCodeList = new ArrayList<>();
    private ArrayList<String> exchangeList = new ArrayList<>();
    private ArrayList<String> protectBubbleList = new ArrayList<>();
    private int exchangeDoubleCard = 0;
    private int exchangeTimes = 0;
    private int exchangeTimesLongTime = 0;
    private int doubleTimes = 0;
    /**
     * 新村-罚单已贴完的用户
     */
    private ArrayList<String> canPasteTicketTime = new ArrayList<>();

    // farm
    private ArrayList<String> answerQuestionList = new ArrayList<>();
    private String questionHint;
    private ArrayList<FeedFriendLog> feedFriendLogList = new ArrayList<>();
    private ArrayList<VisitFriendLog> visitFriendLogList = new ArrayList<>();
    private ArrayList<StallShareIdLog> stallShareIdLogList = new ArrayList<>();
    private ArrayList<StallHelpedCountLog> stallHelpedCountLogList = new ArrayList<>();
    private Set<String> dailyAnswerList = new HashSet<>();
    private ArrayList<String> donationEggList = new ArrayList<>();
    private ArrayList<String> spreadManureList = new ArrayList<>();
    private ArrayList<String> stallP2PHelpedList = new ArrayList<>();
    /**
     * 新村助力好友，已上限的用户
     */
    private List<String> antStallAssistFriend = new ArrayList<>();

    // other
    private ArrayList<String> memberSignInList = new ArrayList<>();
    private int kbSignIn = 0;
    /**
     * 绿色经营，收取好友金币已完成用户
     */
    private List<String> greenFinancePointFriend = new ArrayList<>();

    /**
     * 绿色经营，评级领奖已完成用户
     */
    private Map<String, Integer> greenFinancePrizesMap = new HashMap<String, Integer>();


    public static boolean canWaterFriendToday(String id, int count) {
        id = UserIdMap.getCurrentUid() + "-" + id;
        Status stat = INSTANCE;
        int index = -1;
        for (int i = 0; i < stat.waterFriendLogList.size(); i++)
            if (stat.waterFriendLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0)
            return true;
        WaterFriendLog wfl = stat.waterFriendLogList.get(index);
        return wfl.waterCount < count;
    }

    public static void waterFriendToday(String id, int count) {
        id = UserIdMap.getCurrentUid() + "-" + id;
        Status stat = INSTANCE;
        WaterFriendLog wfl;
        int index = -1;
        for (int i = 0; i < stat.waterFriendLogList.size(); i++)
            if (stat.waterFriendLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0) {
            wfl = new WaterFriendLog(id);
            stat.waterFriendLogList.add(wfl);
        } else {
            wfl = stat.waterFriendLogList.get(index);
        }
        wfl.waterCount = count;
        save();
    }

    public static int getReserveTimes(String id) {
        Status stat = INSTANCE;
        int index = -1;
        for (int i = 0; i < stat.reserveLogList.size(); i++)
            if (stat.reserveLogList.get(i).projectId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0)
            return 0;
        ReserveLog rl = stat.reserveLogList.get(index);
        return rl.applyCount;
    }

    public static boolean canReserveToday(String id, int count) {
        return getReserveTimes(id) < count;
    }

    public static void reserveToday(String id, int count) {
        Status stat = INSTANCE;
        ReserveLog rl;
        int index = -1;
        for (int i = 0; i < stat.reserveLogList.size(); i++)
            if (stat.reserveLogList.get(i).projectId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0) {
            rl = new ReserveLog(id);
            stat.reserveLogList.add(rl);
        } else {
            rl = stat.reserveLogList.get(index);
        }
        rl.applyCount += count;
        save();
    }

    public static boolean canCooperateWaterToday(String uid, String coopId) {
        return !INSTANCE.cooperateWaterList.contains(uid + "_" + coopId);
    }

    public static void cooperateWaterToday(String uid, String coopId) {
        Status stat = INSTANCE;
        String v = uid + "_" + coopId;
        if (!stat.cooperateWaterList.contains(v)) {
            stat.cooperateWaterList.add(v);
            save();
        }
    }

    public static boolean canAncientTreeToday(String cityCode) {
        return !INSTANCE.ancientTreeCityCodeList.contains(cityCode);
    }

    public static void ancientTreeToday(String cityCode) {
        Status stat = INSTANCE;
        if (!stat.ancientTreeCityCodeList.contains(cityCode)) {
            stat.ancientTreeCityCodeList.add(cityCode);
            save();
        }
    }

    public static boolean canAnswerQuestionToday(String uid) {
        return !INSTANCE.answerQuestionList.contains(uid);
    }

    public static void answerQuestionToday(String uid) {
        Status stat = INSTANCE;
        if (!stat.answerQuestionList.contains(uid)) {
            stat.answerQuestionList.add(uid);
            save();
        }
    }

    public static void setQuestionHint(String s) {
        Status stat = INSTANCE;
        if (stat.questionHint == null) {
            stat.questionHint = s;
            save();
        }
    }

    public static boolean canFeedFriendToday(String id, int count) {
        Status stat = INSTANCE;
        int index = -1;
        for (int i = 0; i < stat.feedFriendLogList.size(); i++)
            if (stat.feedFriendLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0)
            return true;
        FeedFriendLog ffl = stat.feedFriendLogList.get(index);
        return ffl.feedCount < count;
    }

    public static void feedFriendToday(String id) {
        Status stat = INSTANCE;
        FeedFriendLog ffl;
        int index = -1;
        for (int i = 0; i < stat.feedFriendLogList.size(); i++)
            if (stat.feedFriendLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0) {
            ffl = new FeedFriendLog(id);
            stat.feedFriendLogList.add(ffl);
        } else {
            ffl = stat.feedFriendLogList.get(index);
        }
        ffl.feedCount++;
        save();
    }

    public static boolean canVisitFriendToday(String id, int count) {
        id = UserIdMap.getCurrentUid() + "-" + id;
        Status stat = INSTANCE;
        int index = -1;
        for (int i = 0; i < stat.visitFriendLogList.size(); i++)
            if (stat.visitFriendLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0)
            return true;
        VisitFriendLog vfl = stat.visitFriendLogList.get(index);
        return vfl.visitCount < count;
    }

    public static void visitFriendToday(String id, int count) {
        id = UserIdMap.getCurrentUid() + "-" + id;
        Status stat = INSTANCE;
        VisitFriendLog vfl;
        int index = -1;
        for (int i = 0; i < stat.visitFriendLogList.size(); i++)
            if (stat.visitFriendLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0) {
            vfl = new VisitFriendLog(id);
            stat.visitFriendLogList.add(vfl);
        } else {
            vfl = stat.visitFriendLogList.get(index);
        }
        vfl.visitCount = count;
        save();
    }

    public static List<String> stallP2PUserIdList(String uid) {
        List<String> p2pUserIdList = new ArrayList<>();
        Status stat = INSTANCE;
        for (int i = 0; i < stat.stallShareIdLogList.size(); i++)
            if (!stat.stallShareIdLogList.get(i).userId.equals(uid)) {
                p2pUserIdList.add(stat.stallShareIdLogList.get(i).userId);
            }
        return p2pUserIdList;
    }

    public static void stallShareIdToday(String uid, String sid) {
        Status stat = INSTANCE;
        StallShareIdLog ssil;
        int index = -1;
        for (int i = 0; i < stat.stallShareIdLogList.size(); i++)
            if (stat.stallShareIdLogList.get(i).userId.equals(uid)) {
                index = i;
                break;
            }
        if (index < 0) {
            ssil = new StallShareIdLog(uid, sid);
            stat.stallShareIdLogList.add(ssil);
        } else {
            ssil = stat.stallShareIdLogList.get(index);
        }
        ssil.shareId = sid;
        save();
    }

    public static String getStallShareId(String uid) {
        Status stat = INSTANCE;
        int index = -1;
        for (int i = 0; i < stat.stallShareIdLogList.size(); i++)
            if (stat.stallShareIdLogList.get(i).userId.equals(uid)) {
                index = i;
                break;
            }
        if (index < 0) {
            return null;
        } else {
            return stat.stallShareIdLogList.get(index).shareId;
        }
    }

    public static boolean canStallHelpToday(String id) {
        Status stat = INSTANCE;
        int index = -1;
        for (int i = 0; i < stat.stallHelpedCountLogList.size(); i++)
            if (stat.stallHelpedCountLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0)
            return true;
        StallHelpedCountLog shcl = stat.stallHelpedCountLogList.get(index);
        return shcl.helpedCount < 3;
    }

    public static void stallHelpToday(String id, boolean limited) {
        Status stat = INSTANCE;
        StallHelpedCountLog shcl;
        int index = -1;
        for (int i = 0; i < stat.stallHelpedCountLogList.size(); i++)
            if (stat.stallHelpedCountLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0) {
            shcl = new StallHelpedCountLog(id);
            stat.stallHelpedCountLogList.add(shcl);
        } else {
            shcl = stat.stallHelpedCountLogList.get(index);
        }
        if (limited) {
            shcl.helpedCount = 3;
        } else {
            shcl.helpedCount += 1;
        }
        save();
    }

    public static boolean canStallBeHelpToday(String id) {
        Status stat = INSTANCE;
        int index = -1;
        for (int i = 0; i < stat.stallHelpedCountLogList.size(); i++)
            if (stat.stallHelpedCountLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0)
            return true;
        StallHelpedCountLog shcl = stat.stallHelpedCountLogList.get(index);
        return shcl.beHelpedCount < 3;
    }

    public static void stallBeHelpToday(String id, boolean limited) {
        Status stat = INSTANCE;
        StallHelpedCountLog shcl;
        int index = -1;
        for (int i = 0; i < stat.stallHelpedCountLogList.size(); i++)
            if (stat.stallHelpedCountLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0) {
            shcl = new StallHelpedCountLog(id);
            stat.stallHelpedCountLogList.add(shcl);
        } else {
            shcl = stat.stallHelpedCountLogList.get(index);
        }
        if (limited) {
            shcl.beHelpedCount = 3;
        } else {
            shcl.beHelpedCount += 1;
        }
        save();
    }

    public static boolean canMemberSignInToday(String uid) {
        return !INSTANCE.memberSignInList.contains(uid);
    }

    public static void memberSignInToday(String uid) {
        Status stat = INSTANCE;
        if (!stat.memberSignInList.contains(uid)) {
            stat.memberSignInList.add(uid);
            save();
        }
    }

    public static boolean canDonationEgg(String uid) {
        return !INSTANCE.donationEggList.contains(uid);
    }

    public static void donationEgg(String uid) {
        Status stat = INSTANCE;
        if (!stat.donationEggList.contains(uid)) {
            stat.donationEggList.add(uid);
            save();
        }
    }

    public static boolean canSpreadManureToday(String uid) {
        return !INSTANCE.spreadManureList.contains(uid);
    }

    public static void spreadManureToday(String uid) {
        Status stat = INSTANCE;
        if (!stat.spreadManureList.contains(uid)) {
            stat.spreadManureList.add(uid);
            save();
        }
    }

    public static boolean canStallP2PHelpToday(String uid) {
        uid = UserIdMap.getCurrentUid() + "-" + uid;
        return !INSTANCE.stallP2PHelpedList.contains(uid);
    }

    public static void stallP2PHelpeToday(String uid) {
        uid = UserIdMap.getCurrentUid() + "-" + uid;
        Status stat = INSTANCE;
        if (!stat.stallP2PHelpedList.contains(uid)) {
            stat.stallP2PHelpedList.add(uid);
            save();
        }
    }

    /**
     * 是否新村助力已到上限
     *
     * @return true是，false否
     */
    public static boolean canAntStallAssistFriendToday() {
        return INSTANCE.antStallAssistFriend.contains(UserIdMap.getCurrentUid());
    }

    /**
     * 设置新村助力已到上限
     */
    public static void antStallAssistFriendToday() {
        Status stat = INSTANCE;
        String uid = UserIdMap.getCurrentUid();
        if (!stat.antStallAssistFriend.contains(uid)) {
            stat.antStallAssistFriend.add(uid);
            save();
        }
    }

    public static boolean canExchangeToday(String uid) {
        return !INSTANCE.exchangeList.contains(uid);
    }

    public static void exchangeToday(String uid) {
        Status stat = INSTANCE;
        if (!stat.exchangeList.contains(uid)) {
            stat.exchangeList.add(uid);
            save();
        }
    }

    public static boolean canProtectBubbleToday(String uid) {
        return !INSTANCE.protectBubbleList.contains(uid);
    }

    public static void protectBubbleToday(String uid) {
        Status stat = INSTANCE;
        if (!stat.protectBubbleList.contains(uid)) {
            stat.protectBubbleList.add(uid);
            save();
        }
    }

    public static boolean canExchangeDoubleCardToday() {
        Status stat = INSTANCE;
        if (stat.exchangeDoubleCard < Statistics.INSTANCE.getDay().time) {
            return true;
        }
        AntForestV2 task = ModelTask.getModel(AntForestV2.class);
        if (task == null) {
            return false;
        }
        return stat.exchangeTimes < task.getExchangeEnergyDoubleClickCount().getValue();
    }

    public static void exchangeDoubleCardToday(boolean isSuccess) {
        Status stat = INSTANCE;
        if (stat.exchangeDoubleCard != Statistics.INSTANCE.getDay().time) {
            stat.exchangeDoubleCard = Statistics.INSTANCE.getDay().time;
        }
        if (isSuccess) {
            stat.exchangeTimes += 1;
        } else {
            AntForestV2 task = ModelTask.getModel(AntForestV2.class);
            if (task == null) {
                stat.exchangeTimes = 0;
            } else {
                stat.exchangeTimes = task.getExchangeEnergyDoubleClickCount().getValue();
            }
        }
        save();
    }

    public static boolean canExchangeDoubleCardTodayLongTime() {
        Status stat = INSTANCE;
        if (stat.exchangeDoubleCard < Statistics.INSTANCE.getDay().time) {
            return true;
        }
        AntForestV2 task = ModelTask.getModel(AntForestV2.class);
        if (task == null) {
            return false;
        }
        return stat.exchangeTimesLongTime < task.getExchangeEnergyDoubleClickCountLongTime().getValue();
    }

    public static void exchangeDoubleCardTodayLongTime(boolean isSuccess) {
        Status stat = INSTANCE;
        if (stat.exchangeDoubleCard != Statistics.INSTANCE.getDay().time) {
            stat.exchangeDoubleCard = Statistics.INSTANCE.getDay().time;
        }
        if (isSuccess) {
            stat.exchangeTimesLongTime += 1;
        } /*else {
            stat.exchangeTimesLongTime = AntForestV2.exchangeEnergyDoubleClickCountLongTime.getValue();
        }*/
        save();
    }

    /**
     * 罚单是否贴完
     *
     * @return true是，false否
     */
    public static boolean canPasteTicketTime() {
        return INSTANCE.canPasteTicketTime.contains(UserIdMap.getCurrentUid());
    }

    /**
     * 罚单贴完了
     */
    public static void pasteTicketTime() {
        if (INSTANCE.canPasteTicketTime.contains(UserIdMap.getCurrentUid())) {
            return;
        }
        INSTANCE.canPasteTicketTime.add(UserIdMap.getCurrentUid());
        save();
    }

    public static boolean canDoubleToday() {
        AntForestV2 task = ModelTask.getModel(AntForestV2.class);
        if (task == null) {
            return false;
        }
        return INSTANCE.doubleTimes < task.getDoubleCountLimit().getValue();
    }

    public static void DoubleToday() {
        INSTANCE.doubleTimes += 1;
        save();
    }

    public static boolean canKbSignInToday() {
        Status stat = INSTANCE;
        return stat.kbSignIn < Statistics.INSTANCE.getDay().time;
    }

    public static void KbSignInToday() {
        Status stat = INSTANCE;
        if (stat.kbSignIn != Statistics.INSTANCE.getDay().time) {
            stat.kbSignIn = Statistics.INSTANCE.getDay().time;
            save();
        }
    }

    public static Set<String> getDadaDailySet() {
        return INSTANCE.dailyAnswerList;
    }

    public static void setDadaDailySet(Set<String> dailyAnswerList) {
        INSTANCE.dailyAnswerList = dailyAnswerList;
        save();
    }

    public static boolean canSyncStepToday(String uid) {
        Status stat = INSTANCE;
        return !stat.syncStepList.contains(uid);
    }

    public static void SyncStepToday(String uid) {
        Status stat = INSTANCE;
        if (!stat.syncStepList.contains(uid)) {
            stat.syncStepList.add(uid);
            save();
        }
    }

    /**
     * 绿色经营-收好友金币是否做完
     *
     * @return true是，false否
     */
    public static boolean canGreenFinancePointFriend() {
        return INSTANCE.greenFinancePointFriend.contains(UserIdMap.getCurrentUid());
    }

    /**
     * 绿色经营-收好友金币完了
     */
    public static void greenFinancePointFriend() {
        if (canGreenFinancePointFriend()) {
            return;
        }
        INSTANCE.greenFinancePointFriend.add(UserIdMap.getCurrentUid());
        save();
    }

    /**
     * 绿色经营-评级任务是否做完
     *
     * @return true是，false否
     */
    public static boolean canGreenFinancePrizesMap() {
        int week = TimeUtil.getWeekNumber(new Date());
        String currentUid = UserIdMap.getCurrentUid();
        if (INSTANCE.greenFinancePrizesMap.containsKey(currentUid)) {
            Integer storedWeek = INSTANCE.greenFinancePrizesMap.get(currentUid);
            return storedWeek != null && storedWeek == week;
        }
        return false;
    }

    /**
     * 绿色经营-评级任务完了
     */
    public static void greenFinancePrizesMap() {
        if (canGreenFinancePrizesMap()) {
            return;
        }
        INSTANCE.greenFinancePrizesMap.put(UserIdMap.getCurrentUid(), TimeUtil.getWeekNumber(new Date()));
        save();
    }

    public static void dayClear() {
        Log.system(TAG, "重置 status.json");
        Status stat = INSTANCE;
        stat.waterFriendLogList.clear();
        stat.cooperateWaterList.clear();
        stat.syncStepList.clear();
        stat.exchangeList.clear();
        stat.protectBubbleList.clear();
        stat.reserveLogList.clear();
        stat.beachTodayList.clear();
        stat.ancientTreeCityCodeList.clear();
        stat.answerQuestionList.clear();
        stat.feedFriendLogList.clear();
        stat.visitFriendLogList.clear();
        stat.stallHelpedCountLogList.clear();
        stat.questionHint = null;
        stat.donationEggList.clear();
        stat.spreadManureList.clear();
        stat.stallP2PHelpedList.clear();
        stat.memberSignInList.clear();
        stat.kbSignIn = 0;
        stat.exchangeDoubleCard = 0;
        stat.exchangeTimes = 0;
        stat.exchangeTimesLongTime = 0;
        stat.doubleTimes = 0;
        stat.antStallAssistFriend.clear();
        stat.canPasteTicketTime.clear();
        stat.greenFinancePointFriend.clear();
        save();
    }

    public static synchronized Status load() {
        String currentUid = UserIdMap.getCurrentUid();
        try {
            if (StringUtil.isEmpty(currentUid)) {
                Log.i(TAG, "用户为空，状态加载失败");
                throw new RuntimeException("用户为空，状态加载失败");
            }
            File statusFile = FileUtil.getStatusFile(currentUid);
            if (statusFile.exists()) {
                String json = FileUtil.readFromFile(statusFile);
                JsonUtil.MAPPER.readerForUpdating(INSTANCE).readValue(json);
                String formatted = JsonUtil.toJsonString(INSTANCE);
                if (formatted != null && !formatted.equals(json)) {
                    Log.i(TAG, "重新格式化 status.json");
                    Log.system(TAG, "重新格式化 status.json");
                    FileUtil.write2File(formatted, FileUtil.getStatusFile(currentUid));
                }
            } else {
                JsonUtil.MAPPER.updateValue(INSTANCE, new Status());
                Log.i(TAG, "初始化 status.json");
                Log.system(TAG, "初始化 status.json");
                FileUtil.write2File(JsonUtil.toJsonString(INSTANCE), FileUtil.getStatusFile(currentUid));
            }
        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
            Log.i(TAG, "状态文件格式有误，已重置");
            Log.system(TAG, "状态文件格式有误，已重置");
            try {
                JsonUtil.MAPPER.updateValue(INSTANCE, new Status());
                FileUtil.write2File(JsonUtil.toJsonString(INSTANCE), FileUtil.getStatusFile(currentUid));
            } catch (JsonMappingException e) {
                Log.printStackTrace(TAG, e);
            }
        }
        return INSTANCE;
    }

    public static synchronized void unload() {
        try {
            JsonUtil.MAPPER.updateValue(INSTANCE, new Status());
        } catch (JsonMappingException e) {
            Log.printStackTrace(TAG, e);
        }
    }

    private static void save() {
        String json = JsonUtil.toJsonString(INSTANCE);
        Log.system(TAG, "保存 status.json");
        String currentUid = UserIdMap.getCurrentUid();
        if (StringUtil.isEmpty(currentUid)) {
            Log.i(TAG, "用户为空，状态保存失败");
            throw new RuntimeException("用户为空，状态保存失败");
        }
        FileUtil.write2File(json, FileUtil.getStatusFile(currentUid));
    }

    @Data
    private static class WaterFriendLog {
        String userId;
        int waterCount = 0;

        public WaterFriendLog() {
        }

        public WaterFriendLog(String id) {
            userId = id;
        }
    }

    @Data
    private static class ReserveLog {
        String projectId;
        int applyCount = 0;

        public ReserveLog() {
        }

        public ReserveLog(String id) {
            projectId = id;
        }
    }

    @Data
    private static class BeachLog {
        String cultivationCode;
        int applyCount = 0;

        public BeachLog() {
        }

        public BeachLog(String id) {
            cultivationCode = id;
        }
    }

    @Data
    private static class FeedFriendLog {
        String userId;
        int feedCount = 0;

        public FeedFriendLog() {
        }

        public FeedFriendLog(String id) {
            userId = id;
        }
    }

    @Data
    private static class VisitFriendLog {
        String userId;
        int visitCount = 0;

        public VisitFriendLog() {
        }

        public VisitFriendLog(String id) {
            userId = id;
        }
    }

    @Data
    private static class StallShareIdLog {
        String userId;
        String shareId;

        public StallShareIdLog() {
        }

        public StallShareIdLog(String uid, String sid) {
            userId = uid;
            shareId = sid;
        }
    }

    @Data
    private static class StallHelpedCountLog {
        String userId;
        int helpedCount = 0;
        int beHelpedCount = 0;

        public StallHelpedCountLog() {
        }

        public StallHelpedCountLog(String id) {
            userId = id;
        }
    }

}
