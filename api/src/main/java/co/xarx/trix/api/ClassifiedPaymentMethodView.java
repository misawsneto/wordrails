package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassifiedPaymentMethodView {
	public boolean creditCard;
  public boolean debitCard;
  public boolean cash;
  public boolean check;
  public boolean ticket;
}
