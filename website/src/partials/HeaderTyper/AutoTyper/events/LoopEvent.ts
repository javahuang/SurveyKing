import { AutoTyper } from '../index';
import { Event } from './Event';

export class LoopEvent extends Event {
  public config: LoopEventConfigInterface;
  public infinite: boolean;

  constructor(autoTyper: AutoTyper, config: LoopEventConfigInterface = {}) {
    super(autoTyper, false, 'loop');
    this.config = config;
    this.infinite = !config.count;
  }

  public async execute(): Promise<void> {
    const autoTyper = this.autoTyper();
    if (this.config.count) {
      for (let i = 0; i <= this.config.count && autoTyper.isActive; i++) {
        await this.loop();
      }
    } else {
      while (autoTyper.isActive) {
        await this.loop();
      }
    }
  }

  private async loop(): Promise<void> {
    const autoTyper = this.autoTyper();
    autoTyper.queue = [...autoTyper.executedQueue];
    const performEvent = autoTyper.queue.shift();
    if (performEvent) await autoTyper.executeEvent(performEvent);
  }
}

export interface LoopEventConfigInterface {
  count?: number;
}
