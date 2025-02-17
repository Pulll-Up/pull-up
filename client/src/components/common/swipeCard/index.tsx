import { Carousel, CarouselApi, CarouselContent, CarouselItem } from '@/components/ui/carousel';
import { ReactNode, useEffect, useState } from 'react';
import SwipeDots from './SwipeDots';

type examComponent = { id: string; component: ReactNode };
interface SwipeCardProps {
  components: examComponent[];
  dots?: boolean;
}

const SwipeCard = ({ components, dots }: SwipeCardProps) => {
  const [api, setApi] = useState<CarouselApi>();
  const [current, setCurrent] = useState(0);
  const [count, setCount] = useState(0);

  useEffect(() => {
    if (!api) {
      return;
    }

    setCount(api.scrollSnapList().length);
    setCurrent(api.selectedScrollSnap() + 1);

    api.on('select', () => {
      setCurrent(api.selectedScrollSnap() + 1);
    });
  }, [api]);

  return (
    <div className="flex flex-col gap-2">
      <Carousel setApi={setApi} className="flex flex-col justify-center">
        <CarouselContent>
          {components.map(({ id, component }) => (
            <CarouselItem key={id}>{component}</CarouselItem>
          ))}
        </CarouselContent>
      </Carousel>
      {dots && (
        <div className="flex justify-center">
          <SwipeDots current={current} count={count} />
        </div>
      )}
    </div>
  );
};

export default SwipeCard;
