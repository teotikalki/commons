/*
 * Copyright (C) 2003-2013 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.commons.notification.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.exoplatform.commons.api.notification.ArgumentLiteral;
import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.NotificationMessage;
import org.exoplatform.commons.api.notification.service.NotificationManager;

public class NotificationContextImpl implements NotificationContext {
  
  public static final NotificationContext DEFAULT = new NotificationContextImpl();
  
  private Map<String, Object> arguments = new ConcurrentHashMap<String, Object>();
  
  private NotificationMessage notification;
  
  private List<NotificationMessage> notifications;
  
  private Exception exception;

  @Override
  public <T> NotificationContext append(ArgumentLiteral<T> argument, Object value) {
    arguments.put(argument.getKey(), value);

    return this;
  }

  @Override
  public <T> NotificationContext remove(ArgumentLiteral<T> argument) {
    arguments.remove(argument);
    return this;
  }

  @Override
  public void clear() {
    arguments.clear();
  }

  @Override
  public <T> T value(ArgumentLiteral<T> argument) {
    Object value = arguments.get(argument.getKey());
    T got = null;
    
    try {
      got = argument.getType().cast(value);
    } catch (Exception e) {
      return null;
    }
    
    return got;
  }

  @Override
  public NotificationMessage getNotificationMessage() {
    return this.notification;
  }

  @Override
  public void setNotificationMessage(NotificationMessage notification) {
    this.notification = notification;
  }

  @Override
  public NotificationManager getNotificationManager() {
    return null;
  }
  
  public Exception getException() {
    return exception;
  }

  public <T> T getException(Class<T> type) {
    if (type == exception.getClass()) {
      return type.cast(exception);
    }
    
    return null;
    
  }

  public void setException(Throwable t) {
    if (t == null) {
      this.exception = null;
    } else if (t instanceof Exception) {
      this.exception = (Exception) t;
    } else {
      this.exception = new Exception("Exception occurred during execution" ,t);
    }
  }

  @Override
  public boolean isFailed() {
    return getException() != null;
  }

  @Override
  public void setNotificationMessages(List<NotificationMessage> notifications) {
    this.notifications = notifications;
  }

  @Override
  public List<NotificationMessage> getNotificationMessages() {
    return this.notifications;
  }

}