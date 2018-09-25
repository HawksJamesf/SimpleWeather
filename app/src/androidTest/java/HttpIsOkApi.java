import android.content.Context;

import com.google.gson.Gson;
import com.hawksjamesf.simpleweather.bean.RealTimeBean;
import com.hawksjamesf.simpleweather.bean.WeatherData;
import com.hawksjamesf.simpleweather.network.WeatherAPIInterface;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.mock.BehaviorDelegate;

/**
 * Copyright ® $ 2017
 * All right reserved.
 *
 * @author: hawskjamesf
 * @since: Sep/25/2018  Tue
 */
public class HttpIsOkApi implements WeatherAPIInterface {
    BehaviorDelegate<WeatherAPIInterface> delegate;
    Context context;

    public HttpIsOkApi(Context context, BehaviorDelegate<WeatherAPIInterface> delegate) {
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    public Call<RealTimeBean> getRealTimeData(String filename) {
        return null;
    }

    @Override
    public Observable<WeatherData> getCurrentWeatherDate(String city) {
        String file = null;
        try {
            file = RestServiceTestHelper.getStringFromFile(context, "current_data.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        WeatherData data = new Gson().fromJson(file, WeatherData.class);
        return delegate.returningResponse(data).getCurrentWeatherDate(city);
    }
}