export interface User {
  id: number;
  phoneNumber: string;
  fullName: string;
  role: string;
}

export interface AuthResponse {
  token: string;
  userId: number;
  fullName: string;
  phoneNumber: string;
  role: string;
  newUser: boolean;
  city?: string;
}

export interface Product {
  id: number;
  productName: string;
  category: string;
  description?: string;
  unitOfMeasure: string;
  pricePerUnit: number;
  availableQuantity: number;
  minimumOrderQuantity: number;
  productImageUrl?: string;
  isOrganic: boolean;
  isAvailable: boolean;
  harvestDate?: string;
  farmer?: { id: number; fullName: string; phoneNumber: string; city?: string };
  sourceCityAdmin?: { id: number; fullName: string; phoneNumber: string } | null;
  sourceSellRequestId?: number;
}

export interface CartItem {
  product: Product;
  quantity: number;
}

export interface Order {
  id: number;
  orderNumber: string;
  orderStatus: string;
  customer?: { id: number; fullName: string; phoneNumber: string };
  deliveryAddress?: string;
  deliveryCity?: string;
  subtotal: number;
  deliveryCharges: number;
  taxAmount: number;
  totalAmount: number;
  paymentStatus: string;
  paymentMethod: string;
  requestedDeliveryDate?: string;
  requestedDeliveryTimeSlot?: string;
  createdAt: string;
  items: any[];
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export interface SellRequest {
  id: number;
  farmer: { id: number; fullName: string; phoneNumber: string; city?: string };
  productName: string;
  category: string;
  unitOfMeasure: string;
  description?: string;
  quantity: number;
  proposedPricePerUnit: number;
  productImageUrl?: string;
  isOrganic: boolean;
  harvestDate?: string;
  city?: string;
  status: 'SUBMITTED' | 'VISIT_SCHEDULED' | 'APPROVED' | 'REJECTED' | 'PURCHASED' | 'LISTED';
  cityAdmin?: { id: number; fullName: string } | null;
  visitDate?: string;
  visitNotes?: string;
  purchaseDate?: string;
  agreedPricePerUnit?: number;
  rejectionReason?: string;
  reviewedBySuperAdmin?: { id: number; fullName: string } | null;
  linkedProductId?: number;
  createdAt: string;
}

export interface SellRequestDashboard {
  totalSellRequests: number;
  sellRequestsByStatus: { [status: string]: number };
  totalProductsListed: number;
  totalUsers: number;
  totalOrders: number;
  totalOrderRevenue: number;
}

export interface Rating {
  id: number;
  order: any;
  user: any;
  product?: any;
  ratingValue: number;
  feedbackText?: string;
  createdAt: string;
}

export interface FarmerSummary {
  farmer: { id: number; fullName: string; phoneNumber: string; city?: string; address?: string; status: string };
  totalSellRequests: number;
  listedProducts: number;
  pendingRequests: number;
  rejectedRequests: number;
}

export interface DemandRequest {
  id: number;
  raisedBy: { id: number; fullName: string; role: string };
  targetFarmer?: { id: number; fullName: string; phoneNumber: string } | null;
  city?: string;
  productName: string;
  category: string;
  unitOfMeasure: string;
  quantity: number;
  offeredPricePerUnit?: number;
  neededByDate: string;
  notes?: string;
  status: 'OPEN' | 'ACCEPTED' | 'DECLINED' | 'CANCELLED';
  linkedSellRequestId?: number;
  responseNote?: string;
  respondedAt?: string;
  createdAt: string;
}
