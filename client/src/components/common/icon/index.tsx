import React, { SVGProps } from 'react';

interface IconProps extends SVGProps<SVGSVGElement> {
  id: string;
  size?: number;
}

const Icon = ({ id, size = 24, ...props }: IconProps) => {
  return (
    <svg width={size} height={size} viewBox="0 0 24 24" {...props}>
      <use href={`/assets/icons/_sprite.svg#${id}`} />
    </svg>
  );
};

export default Icon;
