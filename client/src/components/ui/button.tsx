import * as React from 'react';
import { Slot } from '@radix-ui/react-slot';
import { cva, type VariantProps } from 'class-variance-authority';

import { cn } from '@/lib/utils';

const buttonVariants = cva(
  'ring-offset-background focus-visible:ring-ring inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:size-4 [&_svg]:shrink-0',
  {
    variants: {
      variant: {
        default: 'bg-primary-500 text-white hover:bg-primary-500/90',
        primary: 'bg-primary-500 text-white hover:bg-primary-500/90',
        secondary: 'text-secondary-foreground bg-secondary-500 hover:bg-secondary-500/80',
        transparent: 'bg-transparent text-primary-500 hover:bg-primary-500/10',
        gray: 'bg-stone-200 text-stone-900 hover:bg-gray-100/80',
      },
      size: {
        default: 'h-10 rounded-[12px] px-4 py-2',
        sm: 'h-9 rounded-[12px] px-3',
        lg: 'h-11 rounded-[12px] px-8',
        fit: 'h-auto w-auto rounded-[12px]',
        icon: 'h-10 w-10',
      },
    },
    defaultVariants: {
      variant: 'default',
      size: 'default',
    },
  },
);

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  asChild?: boolean;
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant, size, asChild = false, ...props }, ref) => {
    const Comp = asChild ? Slot : 'button';
    return <Comp className={cn(buttonVariants({ variant, size, className }))} ref={ref} {...props} />;
  },
);
Button.displayName = 'Button';

export { Button, buttonVariants };
