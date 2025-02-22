import { ReactAdapterElement, RenderHooks } from 'Frontend/generated/flow/ReactAdapter';
import { ReactElement } from 'react';
import { retrieveLaunchParams, InitData } from '@telegram-apps/sdk';
import BeatLoader from 'react-spinners/BeatLoader';

class TelegramAuthComponent extends ReactAdapterElement {
  protected override render(hooks: RenderHooks): ReactElement | null {
    const [tgWebAppData, setTgWebAppData] = hooks.useState<InitData>('tgWebAppData');

    const params = retrieveLaunchParams();
    if (params.tgWebAppData && !tgWebAppData) {
      setTgWebAppData(params.tgWebAppData);
    }

    return (
      <BeatLoader color={"#1CB9B4FF"} size={40} />
    );
  }
}

customElements.define('telegram-auth', TelegramAuthComponent);