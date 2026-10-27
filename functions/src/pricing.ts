/**
 * Authoritative Pricing Engine for S K Sweets & Bakes
 * All amounts are calculated in integer paise.
 */

export type MeasurementType = "WEIGHT" | "VOLUME" | "PIECE" | "FIXED_PACK";

export interface PricingRule {
  measurementType: MeasurementType;
  unitPricePaise: number; // For WEIGHT: paise per kg. For VOLUME: paise per L. For PIECE: paise per piece. For FIXED_PACK: paise per pack.
  minQuantity: number;
  maxQuantity: number;
  quantityIncrement: number;
}

/**
 * Calculates line total in integer paise safely.
 */
export function calculateLineTotalPaise(
  measurementType: MeasurementType,
  unitPricePaise: number,
  quantity: number
): number {
  if (quantity <= 0) {
    throw new Error("Quantity must be greater than zero");
  }

  switch (measurementType) {
    case "WEIGHT":
      // quantity in grams. Price is per kilogram.
      // Price = grams * pricePerKg / 1000
      return Math.round((quantity * unitPricePaise) / 1000);

    case "VOLUME":
      // quantity in millilitres. Price is per litre.
      // Price = ml * pricePerLitre / 1000
      return Math.round((quantity * unitPricePaise) / 1000);

    case "PIECE":
      // quantity in integer pieces. Price is per piece.
      return Math.round(quantity * unitPricePaise);

    case "FIXED_PACK":
      // quantity in packs. Price is per pack.
      return Math.round(quantity * unitPricePaise);

    default:
      throw new Error(`Unsupported measurement type: ${measurementType}`);
  }
}

/**
 * Validates quantity according to product rules.
 */
export function validateQuantityRules(rule: PricingRule, quantity: number): boolean {
  if (quantity < rule.minQuantity) {
    throw new Error(`Quantity ${quantity} is below minimum ${rule.minQuantity}`);
  }
  if (quantity > rule.maxQuantity) {
    throw new Error(`Quantity ${quantity} exceeds maximum ${rule.maxQuantity}`);
  }
  if (rule.quantityIncrement > 0) {
    const remainder = (quantity - rule.minQuantity) % rule.quantityIncrement;
    if (remainder !== 0) {
      throw new Error(
        `Quantity ${quantity} does not align with increment ${rule.quantityIncrement}`
      );
    }
  }
  return true;
}
