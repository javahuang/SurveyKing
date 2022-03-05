import { AutoTyper } from '../index';
import { Event } from './Event';

export class RemoveEvent extends Event {
  public config: RemoveEventConfigInterface;
  public all: boolean;

  constructor(autoTyper: AutoTyper, config: RemoveEventConfigInterface = {}) {
    super(autoTyper, true, 'remove');
    config = {
      timeBetweenLetter: autoTyper.config.delay,
      ...config,
    };
    this.config = config;
    this.all = !config.charCount;
  }

  public async execute(): Promise<void> {
    const autoTyper = this.autoTyper();
    const lettersToRemoveCount = this.config.charCount || autoTyper.text.length;
    let removedLetters = 0;

    return new Promise((resolve) => {
      autoTyper.interval(() => {
        // Remove Char
        const newText = autoTyper.text.slice(0, -1);
        autoTyper.setText(newText);
        removedLetters++;

        // Clear Interval
        if (removedLetters > lettersToRemoveCount) {
          autoTyper.clearInterval();
          resolve(undefined);
        }
      }, this.config.timeBetweenLetter);
    });
  }
}

export interface RemoveEventConfigInterface {
  charCount?: number;
  timeBetweenLetter?: number;
}
