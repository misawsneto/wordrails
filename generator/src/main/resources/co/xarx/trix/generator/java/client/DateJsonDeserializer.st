DateJsonDeserializer(package) ::=

<<package $package$;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

class DateJsonDeserializer implements JsonDeserializer<Date> {
	@Override
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {		
		try {
				Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
								.parse(json.getAsString());
				return date;
			} catch (Exception e){
			return new Date(json.getAsLong());
			}
	}
}>>