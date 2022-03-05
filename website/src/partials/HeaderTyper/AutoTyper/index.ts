import { Event } from "./events/Event";
import { RemoveEvent, RemoveEventConfigInterface } from "./events/RemoveEvent";
import { TypeEvent, TypeEventConfigInterface } from "./events/TypeEvent";
import { SleepEvent, SleepEventConfigInterface } from "./events/SleepEvent";
import { LoopEvent, LoopEventConfigInterface } from "./events/LoopEvent";

export class AutoTyper {
    public activeTimeout?: NodeJS.Timeout | number;
    private activeInterval?: NodeJS.Timer | number;
    public config: AutoTyperConfigInterface;

    public queue: Event[];
    public executedQueue: Event[]; // Necessary for loop function

    public textListener: TextListenerType;
    public text: string;

    public isTyping: boolean;
    public isTypingListener: IsTypingListenerType;

    public isActive: boolean;

    constructor(config: CreateAutoTyperConfigInterface = {}) {
        config = {
            initialText: "",
            delay: 200,
            textListener: () => {
                /* empty function */
            },
            isTypingListener: () => {
                /* empty function */
            },
            ...config
        };
        this.config = {
            initialText: config.initialText,
            delay: config.delay
        };
        this.text = config.initialText;
        this.textListener = config.textListener;
        this.isTypingListener = config.isTypingListener;
        this.queue = [];
        this.executedQueue = [];
        this.isTyping = false;
        this.isActive = false;

        this.textListener(this.text);
        this.isTypingListener(this.isTyping);
    }

    public remove(config: RemoveEventConfigInterface = {}): this {
        this.queue.push(new RemoveEvent(this, config));
        return this;
    }

    public type(config: TypeEventConfigInterface = {}): this {
        this.queue.push(new TypeEvent(this, config));
        return this;
    }

    public loop(config: LoopEventConfigInterface = {}): this {
        this.queue.push(new LoopEvent(this, config));
        return this;
    }

    public sleep(config: SleepEventConfigInterface = {}): this {
        this.queue.push(new SleepEvent(this, config));
        return this;
    }

    public start(): this {
        this.isActive = true;
        this.executeEvents();
        return this;
    }

    public async executeEvents() {
        const performEvent = this.queue.shift();
        if (performEvent) await this.executeEvent(performEvent);
    }

    public async executeEvent(event: Event) {
        if (this.activeInterval) {
            return;
        }

        if (event.isTypeEvent) {
            this.isTyping = true;
            this.isTypingListener(this.isTyping);
        }

        // Execute Event
        await event.execute();
        this.executedQueue.push(event);

        if (event.isTypeEvent) {
            this.isTyping = false;
            this.isTypingListener(this.isTyping);
        }

        // Perform next Event if one is in the queue
        if (this.queue.length > 0 && this.isActive) {
            const performEvent = this.queue.shift();
            if (performEvent) await this.executeEvent(performEvent);
        }
    }

    public setText(text: string) {
        this.text = text;
        this.textListener(text);
    }

    public stop() {
        this.isActive = false;
        this.clearInterval();
        this.clearTimeout();
    }

    public interval(onIntervalCalled: () => void, ms?: number): this {
        if (this.activeInterval !== undefined) return this;
        this.activeInterval = setInterval(() => {
            onIntervalCalled();
        }, ms || 1000);

        return this;
    }

    public clearInterval(): this {
        if (this.activeInterval) {
            clearInterval(this.activeInterval as number);
            this.activeInterval = undefined;
        }
        return this;
    }

    public timeout(onTimeoutCalled: () => void, ms?: number): this {
        if (this.activeTimeout !== undefined) return this;
        this.activeTimeout = setTimeout(() => {
            onTimeoutCalled();
        }, ms || 1000);
    }

    public clearTimeout(): this {
        if (this.activeTimeout) {
            clearTimeout(this.activeTimeout as number);
            this.activeTimeout = undefined;
        }
        return this;
    }
}

export type TextListenerType = (currentText: string) => void;
export type IsTypingListenerType = (isTyping: boolean) => void;

export interface CreateAutoTyperConfigInterface {
    textListener?: TextListenerType;
    isTypingListener?: IsTypingListenerType;
    initialText?: string;
    delay?: number;
}

export interface AutoTyperConfigInterface {
    initialText: string;
    delay: number;
}
