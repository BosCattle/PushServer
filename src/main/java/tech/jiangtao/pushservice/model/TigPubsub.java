package tech.jiangtao.pushservice.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @class: TigPubsub </br>
 * @description:  </br>
 * @creator: kevin </br>
 * @email: jiangtao103cp@gmail.com </br>
 * @date: 04/05/2017 16:42</br>
 * @version: 0.0.1 </br>
 **/
public class TigPubsub implements Serializable{

  public int id;
  public String type;
  public String body;
  public String message;
  public Date date;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}
