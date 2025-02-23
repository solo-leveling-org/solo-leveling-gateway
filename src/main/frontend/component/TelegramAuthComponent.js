import React from 'react';
import { ReactAdapterElement } from 'Frontend/generated/flow/ReactAdapter';
import { retrieveLaunchParams } from '@telegram-apps/sdk';
import BeatLoader from 'react-spinners/BeatLoader';

class TelegramAuthComponent extends ReactAdapterElement {
  render(hooks) {
    const [tgAuthData, setTgAuthData] = hooks.useState('tgAuthData');

    const params = retrieveLaunchParams();

    const updatedData = {
      initData: window.Telegram.WebApp.initData,
      tgWebAppData: params.tgWebAppData,
    };

    if (!tgAuthData) {
      setTgAuthData(updatedData);
    }

    return React.createElement(BeatLoader, { color: '#1CB9B4FF', size: 40 });
  }
}

customElements.define('telegram-auth', TelegramAuthComponent);