import { AutoTyper } from '../index';
import { Event } from './Event';

export class SleepEvent extends Event {
  public config: SleepEventConfigInterface;

  constructor(autoTyper: AutoTyper, config: SleepEventConfigInterface = {}) {
    super(autoTyper, false, 'sleep');
    config = {
      ms: 2000,
      ...config,
    };
    this.config = config;
  }

  public async execute(): Promise<void> {
    const autoTyper = this.autoTyper();
    return new Promise((resolve) => {
      autoTyper.timeout(() => {
        autoTyper.clearTimeout();
        resolve(undefined);
      }, this.config.ms);
    });
  }
}

export interface SleepEventConfigInterface {
  ms?: number;
}
