import { ReactAdapterElement, RenderHooks } from 'Frontend/generated/flow/ReactAdapter';
import { ReactElement } from 'react';
import TelegramLoginBtn, { TelegramLoggedUser } from 'telegram-react-auth-button';

class TelegramLoginBtnElement extends ReactAdapterElement {
  protected override render(hooks: RenderHooks): ReactElement | null {
    const [, setUser] = hooks.useState<TelegramLoggedUser>('onAuth');

    const handleAuth = (data: TelegramLoggedUser) => {
      setUser(data);
    };

    return (
      <TelegramLoginBtn
        botName="solo_leveling_app_bot"
        onAuth={handleAuth}
        size="large"
        cornerRadius={20}
        showUserPhoto={true}
        requestAccessToWrite={true}
      />
    );
  }
}

customElements.define('telegram-login-button', TelegramLoginBtnElement);