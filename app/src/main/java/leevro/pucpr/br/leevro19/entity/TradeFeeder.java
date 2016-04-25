package leevro.pucpr.br.leevro19.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jonas on 23/04/2016.
 */
public class TradeFeeder {

    public static Trade tradeFromJSONObject(JSONObject jTrade) {
        Trade trade = new Trade();

        try {
            trade.setFirstUserId(jTrade.getInt("first_user_id"));
            trade.setSecondUserId(jTrade.getInt("second_user_id"));
//            trade.setFirstUser(jTrade.getString("user_id"));
//            trade.setSecondUser(jTrade.getString("isbn"));
            trade.setFirstUserRequestedBookId(jTrade.getInt("first_user_requested_fbook_id"));
            trade.setSecondUserRequestedBookId(jTrade.getInt("second_user_requested_fbook_id"));
//            trade.setFirstUserRequestedBook(jTrade.getString("ftrade_id"));
//            trade.setSecondUserRequestedBook(jTrade.getString("vtrade_id"));
            trade.setFirstUserAccept(jTrade.getBoolean("first_user_accept"));
            trade.setSecondUserAccept(jTrade.getBoolean("second_user_accept"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trade;
    }

    public static TradeCollection tradesFromJSONArray(JSONArray tradeArr) throws JSONException {
        TradeCollection trades = new TradeCollection();

        int size = tradeArr.length();
        for (int i = 0; i < size; i++) {
            trades.add(tradeFromJSONObject(tradeArr.getJSONObject(i)));
        }
        return trades;
    }

}
