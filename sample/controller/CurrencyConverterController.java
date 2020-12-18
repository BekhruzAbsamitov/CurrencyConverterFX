package sample.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.istack.internal.NotNull;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import sample.model.Currency;
import sample.util.DateUtil;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class CurrencyConverterController {

    @FXML
    public Label usdFromValue;
    @FXML
    public Label rubFromValue;
    @FXML
    public Label eurFromValue;
    @FXML
    public Button refreshButton;
    @FXML
    public Label usd;
    @FXML
    public Button convert;
    @FXML
    private Label usdSum;
    @FXML
    private Label usdChanging;
    @FXML
    private TextArea textValue;

    @FXML
    AnchorPane mainPane;

    @FXML
    Pane rubPane;
    @FXML
    Label rub;
    @FXML
    Label rubSum;
    @FXML
    Label rubChanging;

    @FXML
    Pane euroPane;
    @FXML
    Label euro;
    @FXML
    Label euroSum;
    @FXML
    Label euroChanging;

    @FXML
    Label today;
    @FXML
    Label dataDate;
    @FXML
    private ComboBox<String> selectCurrency;

    @NotNull
    private List<Currency> currencies;

    public void getData() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            URL url = new URL("https://cbu.uz/uz/arkhiv-kursov-valyut/json/");
            URLConnection urlConnection = url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            Type type = new TypeToken<List<Currency>>() {
            }.getType();

            currencies = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataDate.setText(Objects.requireNonNull(DateUtil.parse(currencies.get(0).getDate())).toString());
        getData(currencies);


    }

    public static String separateNumber(double num) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat df = new DecimalFormat("###,###.00", symbols);
        return df.format(num);
    }

    @FXML
    public void setChoiceBox() {

        ObservableList<String> currencyNames = FXCollections.observableArrayList();
        for (Currency currency : currencies) {
            currencyNames.add(currency.getCcy() + " - " + currency.getCcyNm_EN());
        }
        selectCurrency.setItems(currencyNames);
    }

    @FXML
    private void convert() {
        if (textValue.getText() != null && selectCurrency.getValue() != null) {
            try {
                String convertFrom = selectCurrency.getValue().substring(0, selectCurrency.getValue().indexOf("-")).trim();
                double textAreaValue = Double.parseDouble(textValue.getText());
                for (Currency currency : currencies) {
                    if (currency.getCcy().equals(convertFrom)) {
                        double rate = Double.parseDouble(currency.getRate());
                        for (Currency currency1 : currencies) {
                            switch (currency1.getCcy()) {
                                case "USD": {
                                    double rate1 = Double.parseDouble(currency1.getRate());
                                    usdFromValue.setText(separateNumber((rate / rate1) * textAreaValue) + " " + convertFrom);
                                    break;
                                }
                                case "RUB": {
                                    double rate1 = Double.parseDouble(currency1.getRate());
                                    rubFromValue.setText(String.format("%.2f", ((rate / rate1) * textAreaValue)) + " " + convertFrom);
                                    break;
                                }
                                case "EUR": {
                                    double rate1 = Double.parseDouble(currency1.getRate());
                                    eurFromValue.setText(String.format("%.2f", ((rate / rate1) * textAreaValue)) + " " + convertFrom);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                throwWarning("You entered inappropriate value.", "Try again!");
            }
        } else {
            throwWarning("Currency not selected!", "Please fill fields.");
        }
    }

    private void throwWarning(String title, String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setX(500);
        alert.setY(200);
        alert.setHeaderText(header);

        alert.showAndWait();
    }

    @FXML
    public void refreshData() {
        getData();
    }

    public void getData(List<Currency> currencies) {

        for (Currency currency : currencies) {
            if (currency.getCcy().equals("USD")) {
                usd.setText(currency.getCcy());
                usdSum.setText(currency.getRate() + " so'm");

                if (Double.parseDouble(currency.getDiff()) >= 0) {
                    usdChanging.setText("+" + currency.getDiff());
                    usdChanging.setTextFill(Color.BLUE);
                } else {
                    usdChanging.setTextFill(Color.RED);
                }
            } else if (currency.getCcy().equals("RUB")) {
                rub.setText(currency.getCcy());
                rubSum.setText(currency.getRate() + " so'm");

                if (Double.parseDouble(currency.getDiff()) >= 0) {
                    rubChanging.setText("+" + currency.getDiff());
                    rubChanging.setTextFill(Color.BLUE);
                } else {
                    rubChanging.setTextFill(Color.RED);
                }
            } else if (currency.getCcy().equals("EUR")) {
                euro.setText(currency.getCcy());
                euroSum.setText(currency.getRate() + " so'm");

                if (Double.parseDouble(currency.getDiff()) >= 0) {
                    euroChanging.setText("+" + currency.getDiff());
                    euroChanging.setTextFill(Color.BLUE);
                } else {
                    euroChanging.setTextFill(Color.RED);
                }
            }
        }
        today.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM, yyyy")));
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }
}
