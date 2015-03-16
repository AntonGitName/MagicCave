package edu.amd.spbstu.magiccave.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

/**
 * @author iAnton
 * @since 09/03/15
 */
public class CandlePuzzle {

    private static final String X_KEY = "x";
    private static final String Y_KEY = "y";
    private static final String ID_KEY = "id";
    private static final String STATE_KEY = "state";
    private static final String IS_CORRECT_KEY = "isCorrect";
    private static final String NEIGHBOURS_KEY = "neighbours";
    private static final String WIDTH_KEY = "width";
    private static final String HEIGHT_KEY = "height";
    private static final String CANDLES_KEY = "candles";

    private final List<CandleModel> candles;
    private final int gridW;
    private final int gridH;

    public CandlePuzzle(String jsonString) {
        InputStream is = new ByteArrayInputStream(jsonString.getBytes());
        final JsonReader jsonReader = Json.createReader(is);
        final JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gridW = jsonObject.getInt(WIDTH_KEY);
        gridH = jsonObject.getInt(HEIGHT_KEY);
        candles = new ArrayList<>();
        final JsonArray jsonCandles = jsonObject.getJsonArray(CANDLES_KEY);
        final int n = jsonCandles.size();
        for (int i = 0; i < n; ++i) {
            final JsonObject jsonCandle = jsonCandles.getJsonObject(i);
            final int x = jsonCandle.getInt(X_KEY);
            final int y = jsonCandle.getInt(Y_KEY);
            final int id = jsonCandle.getInt(ID_KEY);
            final boolean isInvertedCorrectly = jsonCandle.getBoolean(IS_CORRECT_KEY);
            final String state = jsonCandle.getString(STATE_KEY);
            candles.add(new CandleModel(x, y, id, isInvertedCorrectly, state));
        }
        for (int i = 0; i < n; ++i) {
            final JsonObject jsonCandle = jsonCandles.getJsonObject(i);
            final JsonArray jsonArray = jsonCandle.getJsonArray(NEIGHBOURS_KEY);
            final CandleModel model = getCandle(jsonCandle.getInt(ID_KEY));
            for (JsonValue jsonValue : jsonArray) {
                model.addNeighbour(getCandle(Integer.parseInt(jsonValue.toString())));
            }
        }
    }

    public CandlePuzzle(List<CandleModel> candles, int w, int h) {
        this.candles = candles;
        gridW = w;
        gridH = h;
    }

    private CandleModel getCandle(int id) {
        for (CandleModel model : candles) {
            if (id == model.getId()) {
                return model;
            }
        }
        throw new IllegalStateException("Invalid id");
    }

    public List<Integer> getSolution() {
        List<Integer> solution = new ArrayList<>();
        for (CandleModel candle : candles) {
            if (!candle.isInvertedCorrectly()) {
                solution.add(candle.getId());
            }
        }
        return solution;
    }

    public boolean isSolved() {
        for (CandleModel candle : candles) {
            if (candle.getState() == CandleModel.State.OFF) {
                return false;
            }
        }
        return true;
    }

    public List<CandleModel> getCandles() {
        return candles;
    }

    @Override
    public String toString() {
        final JsonBuilderFactory factory = Json.createBuilderFactory(null);
        final JsonArrayBuilder candlesArrayBuilder = factory.createArrayBuilder();
        for (CandleModel model : candles) {
            final JsonArrayBuilder neighboursBuilder = factory.createArrayBuilder();
            for (CandleModel neighbour : model.getNeighbours()) {
                neighboursBuilder.add(neighbour.getId());
            }
            candlesArrayBuilder.add(factory.createObjectBuilder()
                    .add(X_KEY, model.getX())
                    .add(Y_KEY, model.getY())
                    .add(ID_KEY, model.getId())
                    .add(STATE_KEY, model.getState().toString())
                    .add(IS_CORRECT_KEY, model.isInvertedCorrectly())
                    .add(NEIGHBOURS_KEY, neighboursBuilder));
        }
        JsonObject value = factory.createObjectBuilder()
                .add(WIDTH_KEY, gridW)
                .add(HEIGHT_KEY, gridH)
                .add(CANDLES_KEY, candlesArrayBuilder)
                .build();

        return value.toString();
    }
}
